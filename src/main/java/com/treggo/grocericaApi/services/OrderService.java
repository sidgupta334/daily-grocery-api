package com.treggo.grocericaApi.services;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.treggo.grocericaApi.entities.Address;
import com.treggo.grocericaApi.entities.AddressBckp;
import com.treggo.grocericaApi.entities.Cart;
import com.treggo.grocericaApi.entities.Coupon;
import com.treggo.grocericaApi.entities.Orders;
import com.treggo.grocericaApi.entities.Product;
import com.treggo.grocericaApi.entities.Users;
import com.treggo.grocericaApi.enums.OrderStatus;
import com.treggo.grocericaApi.enums.PaymentMethod;
import com.treggo.grocericaApi.repositories.AddressRepository;
import com.treggo.grocericaApi.repositories.AdressBckpRepository;
import com.treggo.grocericaApi.repositories.CartRepository;
import com.treggo.grocericaApi.repositories.OrderRepository;
import com.treggo.grocericaApi.requests.CancelOrderDTO;
import com.treggo.grocericaApi.requests.NewOrderDTO;
import com.treggo.grocericaApi.requests.OrderPaymentSuccessDTO;
import com.treggo.grocericaApi.responses.CartResponse;
import com.treggo.grocericaApi.responses.ChartsResponse;
import com.treggo.grocericaApi.responses.DashboardResponse;
import com.treggo.grocericaApi.responses.OrderResponse;

@Service
public class OrderService {

	@Autowired
	private OrderRepository repo;

	@Autowired
	private CartRepository cRepo;

	@Autowired
	private AddressRepository aRepo;

	@Autowired
	private AdressBckpRepository abRepo;

	@Autowired
	private CartService cService;

	@Autowired
	private ProductService pService;
	
	@Autowired
	private CouponService couponService;

	private String[] productsArray;
	private String[] quantityArray;

	Logger logger = LoggerFactory.getLogger(OrderService.class);

	public List<OrderResponse> createNewOrder(NewOrderDTO req, Users user) {
		if (req.getAddressId() == null || req.getFinalTotal() == null || req.getPaymentMethod() == null) {
			return null;
		}
		try {
			Cart cart = cRepo.fetchByUserId(user.getUserId());
			Address address = aRepo.fetchByAddressId(req.getAddressId());
			if (cart.getNetQuantity() == 0) {
				return null;
			}
			Orders order = new Orders();
			BeanUtils.copyProperties(req, order);
			BeanUtils.copyProperties(cart, order);
			if (req.getFinalTotal() > cart.getNetTotal()) {
				order.setFinalTotal(cart.getNetTotal());
				order.setDiscountApplied(new Long(0));
			} else {
				order.setDiscountApplied(cart.getNetTotal() - req.getFinalTotal());
			}
			if (req.getPaymentMethod().equals(PaymentMethod.COD)) {
				order.setOrderStatus(OrderStatus.ORDERED);
				LocalDate today = LocalDate.now();
				order.setDeliveryDate(today.plusDays(2));
			} else {
				order.setOrderStatus(OrderStatus.CREATED);
			}
			if(req.getCouponApplied() != "") {
				order.setCouponApplied(req.getCouponApplied());
			}
			order.setOrderDate(LocalDate.now());
			order.setSafeDelivery(req.isSafeDelivery());
			order.setTotal(cart.getNetTotal());
			order.setUser(user);
			this.splitAggregateOrders(order);

			// Split orders based on vendors
			List<Orders> orders = this._splitOrders(order);
			List<OrderResponse> resp = new ArrayList<OrderResponse>();
			for (Orders or : orders) {
				OrderResponse res = new OrderResponse();
				repo.save(or);
				BeanUtils.copyProperties(or, res);
				BeanUtils.copyProperties(address, res);
				res.setUserId(user.getUserId());
				res.setFullName(user.getFullName());
				res.setEmail(user.getEmail());
				res.setMapUrl(this.generateMapUrl(res.getLattitude(), res.getLongitude()));
				CartResponse cartResp = cService.mapToCart(cart);
				res.setProducts(cartResp.getProducts());
				resp.add(res);
			}
			cService.emptyCart(user);
			return resp;

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Failed to run: " + e);
			return null;
		}
	}

	private List<Orders> _splitOrders(Orders order) {
		List<Orders> orders = new ArrayList<Orders>();
		String[] productIds = this.splitProducts(order);

		// Iterate over each product and check if they belongs to which vendor:
		Map<Long, String> vendorProductMapping = new HashMap<Long, String>();
		for (int i = 0; i < productIds.length; i++) {
			Product pr = pService.findProduct(new Long(productIds[i]));

			if (vendorProductMapping.containsKey(pr.getVendorId())) {
				String existingProducts = vendorProductMapping.get(pr.getVendorId());
				vendorProductMapping.put(pr.getVendorId(),
						existingProducts.concat("|").concat(pr.getProductId().toString()));
			} else {
				vendorProductMapping.put(pr.getVendorId(), pr.getProductId().toString());
			}
		}

		vendorProductMapping.forEach((vendorId, products) -> {
			Orders or = new Orders();
			BeanUtils.copyProperties(order, or);
			try {
				Coupon applied = couponService.findCoupon(order.getCouponApplied());
				if(applied != null && applied.getVendorId().equals(vendorId)) {
					or.setCouponApplied(applied.getCouponName());
				} else {
					or.setCouponApplied(null);
				}
			} catch(Exception e) {
				logger.error("Error fetching coupon on creating order: " + e.getMessage());
				e.printStackTrace();
				or.setCouponApplied(null);
			}
			or.setVendorId(vendorId);
			or.setProductIds(products);
			or.setProductQuantities(this.getQuantitiesOnProducts(or));
			or.setNetQuantity(this.calculateNetQuantity(or));
			or.setOldTotal(this.calculateOldTotal(or));
			Long nonDiscountedTotal = this.calculateFinalTotal(or);
			if(or.getCouponApplied() != null) {
				or.setFinalTotal(nonDiscountedTotal - order.getDiscountApplied());
				or.setDiscountApplied(order.getDiscountApplied());
			} else {
				or.setFinalTotal(nonDiscountedTotal);
				or.setDiscountApplied(new Long(0));
			}
			orders.add(or);
		});

		return orders;
	}

	private void splitAggregateOrders(Orders order) {
		this.productsArray = this.splitProducts(order);
		this.quantityArray = this.splitProductsQuantites(order);
	}

	private String getQuantitiesOnProducts(Orders order) {
		String[] prIds = this.splitProducts(order);
		String[] quantities = new String[prIds.length];
		for (int i = 0; i < prIds.length; i++) {
			int index = ArrayUtils.indexOf(this.productsArray, prIds[i]);
			if (index != -1) {
				quantities[i] = this.quantityArray[index];
			}
		}
		return String.join("|", quantities);
	}

	private Long calculateOldTotal(Orders order) {
		Long total = new Long(0);
		String[] productIds = this.splitProducts(order);
		String[] productQuantities = this.splitProductsQuantites(order);
		try {
			for (int i = 0; i < productIds.length; i++) {
				Product pr = pService.findProduct(Long.parseLong(productIds[i]));
				total += (pr.getOldPrice() * Long.parseLong(productQuantities[i]));
			}
			return total;
		} catch (Exception e) {
			logger.error("Something went wrong" + e.getMessage());
			e.printStackTrace();
			return null;
		}
	}

	private Long calculateFinalTotal(Orders order) {
		Long total = new Long(0);
		String[] productIds = this.splitProducts(order);
		String[] productQuantities = this.splitProductsQuantites(order);
		try {
			for (int i = 0; i < productIds.length; i++) {
				Product pr = pService.findProduct(Long.parseLong(productIds[i]));
				total += (pr.getNewPrice() * Long.parseLong(productQuantities[i]));
			}
			return total;
		} catch (Exception e) {
			logger.error("Something went wrong" + e.getMessage());
			e.printStackTrace();
			return null;
		}
	}

	private Long calculateNetQuantity(Orders order) {
		Long quantity = new Long(0);
		String[] productQuantities = this.splitProductsQuantites(order);
		for (String pr : productQuantities) {
			quantity += Long.parseLong(pr);
		}
		return quantity;
	}

	private String[] splitProducts(Orders order) {
		if (order.getProductIds().contentEquals("")) {
			return new String[0];
		} else {
			return order.getProductIds().split("\\|");
		}
	}

	private String[] splitProductsQuantites(Orders order) {
		if (order.getProductQuantities().contentEquals("")) {
			return new String[0];
		} else {
			return order.getProductQuantities().split("\\|");
		}
	}

	public OrderResponse viewOrder(Long orderId, Users user) {
		try {
			Orders order = repo.findByOrderId(orderId);
			return mapOrderResponse(order, user);
		} catch (Exception e) {
			logger.error("Failed to run: " + e);
			return null;
		}
	}

	public List<OrderResponse> getAllOrdersOfUser(Users user) {
		try {
			List<Orders> orders = repo.fetchByUserId(user.getUserId());
			List<OrderResponse> resp = new ArrayList<OrderResponse>();
			for (Orders o : orders) {
				resp.add(mapOrderResponse(o, user));
			}
			return resp;
		} catch (Exception e) {
			logger.error("Failed to run: " + e);
			return null;
		}
	}

	public OrderResponse successPayment(OrderPaymentSuccessDTO req, Users user) {
		if (req.getOrderId() == null || req.getPaymentMode() == null || req.getTransactionId() == null) {
			return null;
		}
		try {
			Orders order = repo.findByOrderId(req.getOrderId());
			if (order.getPaymentMethod().equals(PaymentMethod.COD)) {
				return null;
			}

			if (order.getOrderStatus().equals(OrderStatus.CREATED)) {
				order.setOrderStatus(OrderStatus.ORDERED);
				order.setOnlinePaymentMode(req.getPaymentMode());
				order.setTransactionId(req.getTransactionId());
				try {
					repo.save(order);
					return mapOrderResponse(order, user);
				} catch (Exception e) {
					return null;
				}
			} else {
				return null;
			}
		}

		catch (Exception e) {
			logger.error("Failed to run: " + e);
			return null;
		}
	}

	public boolean confirmOrder(Long orderId) {
		Orders order = repo.findByOrderId(orderId);
		if (order == null) {
			return false;
		}
		if (order.getOrderStatus().equals(OrderStatus.ORDERED)) {
			order.setOrderStatus(OrderStatus.CONFIRMED);
			try {
				repo.save(order);
				return true;
			} catch (Exception e) {
				logger.error("Failed to run: " + e);
				return false;
			}
		} else {
			return false;
		}
	}

	public boolean deliverOrder(Long orderId) {
		Orders order = repo.findByOrderId(orderId);
		if (order == null) {
			return false;
		}
		if (order.getOrderStatus().equals(OrderStatus.CONFIRMED)) {
			order.setOrderStatus(OrderStatus.DELIVERED);
			try {
				repo.save(order);
				return true;
			} catch (Exception e) {
				logger.error("Failed to run: " + e);
				return false;
			}
		} else {
			logger.error("Order can't be delivered because order status is: " + order.getOrderStatus().toString());
			return false;
		}
	}

	public OrderResponse cancelOrder(CancelOrderDTO req, Users user) {
		if (req.getOrderId() == null || req.getCancellationReason() == null) {
			return null;
		}
		try {
			Orders order = repo.findByOrderId(req.getOrderId());
			if (order.getOrderStatus().equals(OrderStatus.COMPLETED)
					|| order.getOrderStatus().equals(OrderStatus.CONFIRMED)) {
				return null;
			}
			if (order.getPaymentMethod().equals(PaymentMethod.COD)) {
				order.setOrderStatus(OrderStatus.CANCELLED_COD);
			} else {
				order.setOrderStatus(OrderStatus.CANCELLED_ONLINE);
			}
			order.setCancellationReason(req.getCancellationReason());
			order.setDeliveryDate(null);
			repo.save(order);
			return mapAdminResponse(order);
		} catch (Exception e) {
			logger.error("Failed to run: " + e);
			return null;
		}
	}

	public List<OrderResponse> fetchOpenOrders() {
		List<Orders> resp = repo.fetchByMultipleOrders(OrderStatus.CREATED.toString(), OrderStatus.ORDERED.toString(),
				OrderStatus.CONFIRMED.toString(), OrderStatus.DELIVERED.toString());
		List<OrderResponse> res = new ArrayList<OrderResponse>();
		for (Orders o : resp) {
			res.add(mapAdminResponse(o));
		}
		return res;
	}

	public List<OrderResponse> fetchOpenVendorOrders(Long vendorId) {
		List<Orders> resp = repo.fetchByMultipleOrders(OrderStatus.CREATED.toString(), OrderStatus.ORDERED.toString(),
				OrderStatus.CONFIRMED.toString(), OrderStatus.DELIVERED.toString());
		List<OrderResponse> res = new ArrayList<OrderResponse>();
		for (Orders o : resp) {
			if (o.getVendorId() == vendorId) {
				res.add(mapAdminResponse(o));
			}
		}
		return res;
	}

	public List<OrderResponse> fetchClosedOrders(Long vendorId) {
		List<Orders> orders1;
		if(vendorId == null) {
			orders1 = repo.fetchByOrderStatus(OrderStatus.COMPLETED.toString());
		} else {
			orders1 = repo.fetchByOrderStatusAndVendor(OrderStatus.COMPLETED.toString(), vendorId);
		}
		List<OrderResponse> res = new ArrayList<OrderResponse>();
		for (Orders o : orders1) {
			res.add(mapAdminResponse(o));
		}
		return res;
	}

	public List<OrderResponse> fetchCancelledOrders(Long vendorId) {
		List<Orders> orders1;
		List<Orders> orders2;
		if(vendorId == null) {
			orders1 = repo.fetchByOrderStatus(OrderStatus.CANCELLED_COD.toString());
			orders2 = repo.fetchByOrderStatus(OrderStatus.CANCELLED_ONLINE.toString());
			orders1.addAll(orders2);
		} else {
			orders1 = repo.fetchByOrderStatusAndVendor(OrderStatus.CANCELLED_COD.toString(), vendorId);
			orders2 = repo.fetchByOrderStatusAndVendor(OrderStatus.CANCELLED_ONLINE.toString(), vendorId);
			orders1.addAll(orders2);
		}
		
		List<OrderResponse> res = new ArrayList<OrderResponse>();
		for (Orders o : orders1) {
			res.add(mapAdminResponse(o));
		}
		return res;
	}

	public OrderResponse mapOrderToVendor(Long orderId, Long userId) {
		try {
			Orders order = repo.findByOrderId(orderId);
			if (order == null) {
				return null;
			}
			order.setMappedTo(userId);
			repo.save(order);
			OrderResponse res = mapAdminResponse(order);
			return res;

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public List<OrderResponse> getOrdersOfVendor(Long userId) {
		try {
			List<Orders> orders = repo.fetchByMappedUser(userId);
			List<OrderResponse> res = new ArrayList<OrderResponse>();
			for (Orders o : orders) {
				res.add(mapAdminResponse(o));
			}
			return res;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public List<OrderResponse> getVendorOrderByStatus(Long userId, OrderStatus status1, OrderStatus status2) {
		try {
			List<OrderResponse> res = new ArrayList<OrderResponse>();
			List<Orders> orders1 = repo.fetchByMappedUserAndStatus(userId, status1.toString());
			for (Orders o : orders1) {
				res.add(mapAdminResponse(o));
			}
			if (status2 != null) {
				List<Orders> orders2 = repo.fetchByMappedUserAndStatus(userId, status2.toString());
				for (Orders o : orders2) {
					res.add(mapAdminResponse(o));
				}
			}

			return res;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public List<OrderResponse> filterByPincode(String pincode, Long vendorId) {
		try {
			List<AddressBckp> ad = abRepo.fetchByPinCode(pincode);
			List<Long> addressIds = new ArrayList<Long>();
			for (AddressBckp address : ad) {
				addressIds.add(address.getAddressId());
			}
			List<Orders> orders;
			if(vendorId == null) {
				orders = repo.fetchByAddressId(addressIds);
			} else {
				orders = repo.fetchByAddressIdAndVendor(addressIds, vendorId);
			}
			List<OrderResponse> res = new ArrayList<OrderResponse>();
			for (Orders o : orders) {
				res.add(mapAdminResponse(o));
			}
			return res;
		} catch (Exception e) {
			logger.error("Failed to run: " + e);
			return null;
		}
	}

	public List<OrderResponse> filterByPaymentMode(String onlinePaymentMode, Long vendorId) {
		try {
			List<Orders> orders;
			if(vendorId == null) {
				orders = repo.fetchByPaymentMode(onlinePaymentMode);
			} else {
				orders = repo.fetchByPaymentModeAndVendor(onlinePaymentMode, vendorId);
			}
			List<OrderResponse> res = new ArrayList<OrderResponse>();
			for (Orders o : orders) {
				res.add(mapAdminResponse(o));
			}
			return res;
		} catch (Exception e) {
			logger.error("Failed to run: " + e);
			return null;
		}
	}

	public List<OrderResponse> filterByPaymentMethod(String paymentMethod, Long vendorId) {
		try {
			List<Orders> orders;
			if(vendorId == null) {
				orders = repo.fetchByPaymentMethod(paymentMethod);
			} else {
				orders = repo.fetchByPaymentMethodAndVendor(paymentMethod, vendorId);
			}
			List<OrderResponse> res = new ArrayList<OrderResponse>();
			for (Orders o : orders) {
				res.add(mapAdminResponse(o));
			}
			return res;
		} catch (Exception e) {
			logger.error("Failed to run: " + e);
			return null;
		}
	}

	public List<OrderResponse> filterByTransactionId(String transactionId, Long vendorId) {
		try {
			List<Orders> orders;
			if(vendorId == null) {
				orders = repo.fetchByTransactionId(transactionId);
			} else { 
				orders = repo.fetchByTransactionIdAndVendor(transactionId, vendorId);
			}
			List<OrderResponse> res = new ArrayList<OrderResponse>();
			for (Orders o : orders) {
				res.add(mapAdminResponse(o));
			}
			return res;
		} catch (Exception e) {
			logger.error("Failed to run: " + e);
			return null;
		}
	}

	public List<OrderResponse> filterByDates(LocalDate startDate, LocalDate endDate, Long vendorId) {
		try {
			List<Orders> orders;
			if(vendorId == null) {
				orders = repo.fetchByFilterDates(startDate, endDate);
			} else {
				orders = repo.fetchByFilterDatesAndVendor(startDate, endDate, vendorId);
			}
			List<OrderResponse> res = new ArrayList<OrderResponse>();
			for (Orders o : orders) {
				res.add(mapAdminResponse(o));
			}
			return res;
		} catch (Exception e) {
			logger.error("Failed to run: " + e);
			return null;
		}
	}

	public boolean completeOrder(Long orderId) {
		if (orderId == null) {
			return false;
		}
		try {
			Orders order = repo.findByOrderId(orderId);
			order.setOrderStatus(OrderStatus.COMPLETED);
			repo.save(order);
			return true;
		} catch (Exception e) {
			logger.error("Failed to run: " + e);
			return false;
		}
	}

	public DashboardResponse getDailySales() {
		DashboardResponse res = new DashboardResponse(new Long(0), new Long(0));
		Long count = new Long(0);
		Long amount = new Long(0);
		try {
			List<Orders> orders = repo.fetchAllByFilterDates(LocalDate.now(), LocalDate.now());
			for (Orders order : orders) {
				count += 1;
				amount += order.getFinalTotal();
			}
			res.setAmount(amount);
			res.setOrderCount(count);
			return res;
		} catch (Exception e) {
			logger.error("Failed to run: " + e);
			return res;
		}
	}

	public DashboardResponse getMonthlySales() {
		DashboardResponse res = new DashboardResponse(new Long(0), new Long(0));
		Long count = new Long(0);
		Long amount = new Long(0);
		try {
			LocalDate today = LocalDate.now();
			LocalDate startDate = today.withDayOfMonth(1);
			List<Orders> orders = repo.fetchAllByFilterDates(startDate, today);
			for (Orders order : orders) {
				count += 1;
				amount += order.getFinalTotal();
			}
			res.setAmount(amount);
			res.setOrderCount(count);
			return res;
		} catch (Exception e) {
			logger.error("Failed to run: " + e);
			return res;
		}
	}

	public DashboardResponse getYearlySales() {
		DashboardResponse res = new DashboardResponse(new Long(0), new Long(0));
		Long count = new Long(0);
		Long amount = new Long(0);
		try {
			LocalDate today = LocalDate.now();
			LocalDate startDate = today.withDayOfYear(1);
			List<Orders> orders = repo.fetchAllByFilterDates(startDate, today);
			for (Orders order : orders) {
				count += 1;
				amount += order.getFinalTotal();
			}
			res.setAmount(amount);
			res.setOrderCount(count);
			return res;
		} catch (Exception e) {
			logger.error("Failed to run: " + e);
			return res;
		}
	}

	private OrderResponse mapOrderResponse(Orders order, Users user) {
		try {
			OrderResponse res = new OrderResponse();
			BeanUtils.copyProperties(order, res);
			Address address = aRepo.fetchByAddressId(order.getAddressId());
			if (address == null) {
				AddressBckp bckp = abRepo.fetchByAddressId(order.getAddressId());
				BeanUtils.copyProperties(bckp, res);
			} else {
				BeanUtils.copyProperties(address, res);
			}
			res.setUserId(user.getUserId());
			res.setFullName(user.getFullName());
			res.setEmail(user.getEmail());
			res.setUserId(user.getUserId());
			res.setFullName(user.getFullName());
			res.setEmail(user.getEmail());
			res.setVendorId(order.getVendorId());
			res.setMappedTo(order.getMappedTo());
			Cart cart = cRepo.fetchByUserId(user.getUserId());
			if (cart == null) {
				cart = new Cart();
			}
			cart.setNetTotal(order.getFinalTotal());
			cart.setNetQuantity(order.getNetQuantity());
			cart.setProductIds(order.getProductIds());
			cart.setProductQuantities(order.getProductQuantities());
			CartResponse cartResp = cService.mapToCart(cart);
			res.setProducts(cartResp.getProducts());
			res.setMapUrl(this.generateMapUrl(res.getLattitude(), res.getLongitude()));
			return res;
		} catch (Exception e) {
			logger.error("Failed to run: " + e);
			return null;
		}
	}

	private OrderResponse mapAdminResponse(Orders order) {
		try {
			OrderResponse res = new OrderResponse();
			BeanUtils.copyProperties(order, res);
			Address address = aRepo.fetchByAddressId(order.getAddressId());
			if (address == null) {
				AddressBckp bckp = abRepo.fetchByAddressId(order.getAddressId());
				if (bckp != null) {
					BeanUtils.copyProperties(bckp, res);
				}
			} else {
				BeanUtils.copyProperties(address, res);
			}
			Users user = order.getUser();
			res.setUserId(user.getUserId());
			res.setFullName(user.getFullName());
			res.setEmail(user.getEmail());
			res.setUserId(user.getUserId());
			res.setFullName(user.getFullName());
			res.setEmail(user.getEmail());
			res.setMappedTo(order.getMappedTo());
			Cart cart = cRepo.fetchByUserId(user.getUserId());
			cart.setNetTotal(order.getFinalTotal());
			cart.setNetQuantity(order.getNetQuantity());
			cart.setProductIds(order.getProductIds());
			cart.setProductQuantities(order.getProductQuantities());
			CartResponse cartResp = cService.mapToCart(cart);
			res.setVendorId(order.getVendorId());
			res.setProducts(cartResp.getProducts());
			res.setMapUrl(this.generateMapUrl(res.getLattitude(), res.getLongitude()));
			return res;
		} catch (Exception e) {
			logger.error("Error while mapping response: " + e.getMessage());
			e.printStackTrace();
			return null;
		}
	}

	// Daily view of chart:
	// Used to calculate charts data for daily(7 days), monthly(12 months) and
	// yearly(10 years)
	public List<ChartsResponse> getChartsData(Long vendorId) {

		List<ChartsResponse> response = new ArrayList<>();
		if (vendorId == null) {
			response.add(getDaysResponse(null));
			response.add(getMonthsResponse(null));
			response.add(getYearsResponse(null));
		} else {
			response.add(getDaysResponse(vendorId));
			response.add(getMonthsResponse(vendorId));
			response.add(getYearsResponse(vendorId));
		}

		return response;
	}

	// Calculate daily chart data:
	private ChartsResponse getDaysResponse(Long vendorId) {
		LocalDate today = LocalDate.now();
		ChartsResponse days = new ChartsResponse();
		days.setType("Days");

		LocalDate[] temp = new LocalDate[7];
		Long[] amounts = new Long[7];

		temp[6] = today;
		if (vendorId == null) {
			amounts[6] = calculateAmountInDate(today, today);
		} else {
			amounts[6] = calculateAmountInDateVendor(today, today, vendorId);
		}

		for (int i = 6; i > 0; i--) {
			temp[6 - i] = today.minus(i, ChronoUnit.DAYS);
			if (vendorId == null) {
				amounts[6 - i] = calculateAmountInDate(temp[6 - i], temp[6 - i]);
			} else {
				amounts[6 - i] = calculateAmountInDateVendor(temp[6 - i], temp[6 - i], vendorId);
			}
		}
		days.setLabels(temp);
		days.setData(amounts);
		return days;

	}

	// Calculate monthly chart data:
	private ChartsResponse getMonthsResponse(Long vendorId) {

		LocalDate today = LocalDate.now();
		ChartsResponse days = new ChartsResponse();
		days.setType("Months");

		LocalDate[] temp = new LocalDate[12];
		Long[] amounts = new Long[12];

		LocalDate prevDate = today.minus(12, ChronoUnit.MONTHS);
		for (int i = 12; i > 0; i--) {
			temp[12 - i] = today.minus(i - 1, ChronoUnit.MONTHS);
			if (vendorId == null) {
				amounts[12 - i] = calculateAmountInDate(prevDate, temp[12 - i]);
			} else {
				amounts[12 - i] = calculateAmountInDateVendor(prevDate, temp[12 - i], vendorId);
			}
			prevDate = temp[12 - i];
		}

		days.setLabels(temp);
		days.setData(amounts);
		return days;
	}

	// Calculate yearly chart data:
	private ChartsResponse getYearsResponse(Long vendorId) {

		LocalDate today = LocalDate.now();
		ChartsResponse days = new ChartsResponse();
		days.setType("Years");

		LocalDate[] temp = new LocalDate[10];
		Long[] amounts = new Long[10];

		LocalDate prevDate = today.minus(10, ChronoUnit.YEARS);
		for (int i = 10; i > 0; i--) {
			temp[10 - i] = today.minus(i - 1, ChronoUnit.YEARS);
			if (vendorId == null) {
				amounts[10 - i] = calculateAmountInDate(prevDate, temp[10 - i]);
			} else {
				amounts[10 - i] = calculateAmountInDateVendor(prevDate, temp[10 - i], vendorId);
			}

			prevDate = temp[10 - i];
		}

		days.setLabels(temp);
		days.setData(amounts);
		return days;
	}

	private Long calculateAmountInDate(LocalDate start_date, LocalDate end_date) {
		Long amount = (long) 0;
		List<Orders> orders = repo.fetchAllByFilterDates(start_date, end_date);
		for (Orders ord : orders) {
			amount = amount + ord.getFinalTotal();
		}
		return amount;
	}

	private Long calculateAmountInDateVendor(LocalDate start_date, LocalDate end_date, Long vendorId) {
		Long amount = (long) 0;
		List<Orders> orders = repo.fetchAllByFilterDatesAndVendor(start_date, end_date, vendorId);
		for (Orders ord : orders) {
			amount = amount + ord.getFinalTotal();
		}
		return amount;
	}

	private String generateMapUrl(String lattitude, String longitude) {
		if (lattitude.length() > 0 && longitude.length() > 0) {
			return "https://www.google.com/maps/search/?api=1&query=" + lattitude + "," + longitude;
		} else {
			return "";
		}
	}
}
