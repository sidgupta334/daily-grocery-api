package com.treggo.grocericaApi.controllers;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.treggo.grocericaApi.entities.DeliveryUser;
import com.treggo.grocericaApi.entities.Feedback;
import com.treggo.grocericaApi.entities.Users;
import com.treggo.grocericaApi.enums.OrderStatus;
import com.treggo.grocericaApi.enums.userType;
import com.treggo.grocericaApi.requests.CancelOrderDTO;
import com.treggo.grocericaApi.requests.FeedbackDTO;
import com.treggo.grocericaApi.requests.NewOrderDTO;
import com.treggo.grocericaApi.requests.OrderPaymentSuccessDTO;
import com.treggo.grocericaApi.responses.GeneralResponse;
import com.treggo.grocericaApi.responses.OrderResponse;
import com.treggo.grocericaApi.services.FeedbackService;
import com.treggo.grocericaApi.services.OrderService;
import com.treggo.grocericaApi.services.TokenGenerator;

import io.swagger.annotations.ApiOperation;

@RestController
@CrossOrigin
@RequestMapping("/order")
public class OrderController {

	@Autowired
	private OrderService orderService;

	@Autowired
	private FeedbackService feedbackService;

	@Autowired
	private TokenGenerator tokenService;

	@ApiOperation(value = "Create a new order")
	@PostMapping("/create")
	public ResponseEntity<?> createOrder(@RequestBody NewOrderDTO req, @RequestHeader("token") String token) {

		// Validate token
		Users user = tokenService.validateToken(token);
		if (user == null) {
			return ResponseEntity.status(401).body(new GeneralResponse("Unauthorized"));
		}

		List<OrderResponse> res = orderService.createNewOrder(req, user);
		if (res == null) {
			return ResponseEntity.status(500).body(new GeneralResponse("Failed to create Order"));
		} else {
			return ResponseEntity.ok(res);
		}
	}

	@ApiOperation(value = "View single exiting order")
	@GetMapping("/view/{orderId}")
	public ResponseEntity<?> viewOrder(@PathVariable("orderId") Long orderId, @RequestHeader("token") String token) {
		// Validate token
		Users user = tokenService.validateToken(token);
		if (user == null) {
			return ResponseEntity.status(401).body(new GeneralResponse("Unauthorized"));
		}

		OrderResponse res = orderService.viewOrder(orderId, user);
		if (res == null) {
			return ResponseEntity.status(500).body(new GeneralResponse("Failed to fetch Order"));
		} else {
			return ResponseEntity.ok(res);
		}
	}

	@ApiOperation(value = "Confirm Order after successful payment")
	@PostMapping("/payment/success")
	public ResponseEntity<?> successPayment(@RequestBody OrderPaymentSuccessDTO req,
			@RequestHeader("token") String token) {

		// Validate token
		Users user = tokenService.validateToken(token);
		if (user == null) {
			return ResponseEntity.status(401).body(new GeneralResponse("Unauthorized"));
		}

		OrderResponse res = orderService.successPayment(req, user);
		if (res == null) {
			return ResponseEntity.status(500).body(new GeneralResponse("Failed to confirm Order"));
		} else {
			return ResponseEntity.ok(res);
		}
	}

	@ApiOperation(value = "Cancel any order")
	@PostMapping("/cancel")
	public ResponseEntity<?> cancelOrder(@RequestBody CancelOrderDTO req, @RequestHeader("token") String token) {

		// Validate token
		Users user = tokenService.validateToken(token);
		if (user == null) {
			return ResponseEntity.status(401).body(new GeneralResponse("Unauthorized"));
		}

		OrderResponse res = orderService.cancelOrder(req, user);
		if (res == null) {
			return ResponseEntity.status(500).body(new GeneralResponse("Failed to cancel Order"));
		} else {
			return ResponseEntity.ok(res);
		}
	}

	@ApiOperation(value = "Complete the order after order is successful")
	@GetMapping("/complete/{orderId}")
	public ResponseEntity<?> completeOrder(@PathVariable("orderId") Long orderId,
			@RequestHeader("token") String token) {
		// Validate token
		Users user = tokenService.validateToken(token);
		if (user == null) {
			return ResponseEntity.status(401).body(new GeneralResponse("Unauthorized"));
		}

		boolean res = orderService.completeOrder(orderId);
		if (res) {
			return ResponseEntity.ok(new GeneralResponse("Order completed"));
		} else {
			return ResponseEntity.status(500).body(new GeneralResponse("Failed to complete Order"));
		}
	}

	@ApiOperation(value = "Confirm the order after order is accepted")
	@GetMapping("/confirm/{orderId}")
	public ResponseEntity<?> confirmOrder(@PathVariable("orderId") Long orderId, @RequestHeader("token") String token) {
		// Validate token
		Users user = tokenService.validateToken(token);
		if (user == null) {
			return ResponseEntity.status(401).body(new GeneralResponse("Unauthorized"));
		}

		boolean res = orderService.confirmOrder(orderId);
		if (res) {
			return ResponseEntity.ok(new GeneralResponse("Confirmed"));
		} else {
			return ResponseEntity.status(500).body(new GeneralResponse("Failed to confirm Order"));
		}
	}

	@ApiOperation(value = "Deliver the order by vendor")
	@GetMapping("/deliver/{orderId}")
	public ResponseEntity<?> deliverOrder(@PathVariable("orderId") Long orderId, @RequestHeader("token") String token) {
		// Validate token
		DeliveryUser user = tokenService.validateDeliveryToken(token);
		if (user == null) {
			return ResponseEntity.status(401).body(new GeneralResponse("Unauthorized"));
		}

		boolean res = orderService.deliverOrder(orderId);
		if (res) {
			return ResponseEntity.ok(new GeneralResponse("Delivered"));
		} else {
			return ResponseEntity.status(500).body(new GeneralResponse("Failed to deliver Order"));
		}
	}

	@ApiOperation(value = "Fetch all the open orders")
	@GetMapping("/open")
	public ResponseEntity<?> openOrders(@RequestHeader("token") String token) {
		// Validate token
		Users user = tokenService.validateToken(token);
		if (user != null && user.getUserType().equals(userType.ADMIN)) {
			return ResponseEntity.ok(orderService.fetchOpenOrders());
		} else {
			return ResponseEntity.status(401).body(new GeneralResponse("Unauthorized"));
		}
	}

	@ApiOperation(value = "Fetch all the open orders for vendor")
	@GetMapping("/vendor/open")
	public ResponseEntity<?> openVendorOrders(@RequestHeader("token") String token) {
		// Validate token
		Users user = tokenService.validateToken(token);
		if (user != null && user.getUserType().equals(userType.VENDOR)) {
			return ResponseEntity.ok(orderService.fetchOpenVendorOrders(user.getUserId()));
		} else {
			return ResponseEntity.status(401).body(new GeneralResponse("Unauthorized"));
		}
	}

	@ApiOperation(value = "Fetch all orders of user")
	@GetMapping("/user/all")
	public ResponseEntity<?> allUserOrders(@RequestHeader("token") String token) {
		// Validate token
		Users user = tokenService.validateToken(token);
		if (user == null) {
			return ResponseEntity.status(401).body(new GeneralResponse("Unauthorized"));
		}

		List<OrderResponse> res = orderService.getAllOrdersOfUser(user);
		if (res == null) {
			return ResponseEntity.status(500).body(new GeneralResponse("Failed"));
		} else {
			return ResponseEntity.ok(res);
		}
	}

	@ApiOperation(value = "Fetch all the closed orders")
	@GetMapping("/completed")
	public ResponseEntity<?> completedOrders(@RequestHeader("token") String token) {
		// Validate token
		Users user = tokenService.validateToken(token);
		if (user == null) {
			return ResponseEntity.status(401).body(new GeneralResponse("Unauthorized"));
		} else {
			if (user.getUserType().equals(userType.ADMIN)) {
				return ResponseEntity.ok(orderService.fetchClosedOrders(null));
			} else {
				return ResponseEntity.ok(orderService.fetchClosedOrders(user.getUserId()));

			}
		}
	}

	@ApiOperation(value = "Fetch all the cancelled orders")
	@GetMapping("/cancelled")
	public ResponseEntity<?> cancelledOrders(@RequestHeader("token") String token) {
		// Validate token
		Users user = tokenService.validateToken(token);
		if (user == null) {
			return ResponseEntity.status(401).body(new GeneralResponse("Unauthorized"));
		} else {
			if (user.getUserType().equals(userType.ADMIN)) {
				return ResponseEntity.ok(orderService.fetchCancelledOrders(null));
			} else {
				return ResponseEntity.ok(orderService.fetchCancelledOrders(user.getUserId()));

			}
		}
	}

	@ApiOperation(value = "Filter orders by Pincode")
	@GetMapping("/filter/pincode/{pincode}")
	public ResponseEntity<?> filterByPincode(@PathVariable("pincode") String pincode,
			@RequestHeader("token") String token) {

		// Validate token
		Users user = tokenService.validateToken(token);
		if (user == null) {
			return ResponseEntity.status(401).body(new GeneralResponse("Unauthorized"));
		} else {
			List<OrderResponse> emptyRes = new ArrayList<OrderResponse>();
			if (user.getUserType().equals(userType.ADMIN)) {
				List<OrderResponse> res = orderService.filterByPincode(pincode, null);
				if (res == null) {
					return ResponseEntity.ok(emptyRes);
				} else {
					return ResponseEntity.ok(res);
				}
			} else {
				List<OrderResponse> res = orderService.filterByPincode(pincode, user.getUserId());
				if (res == null) {
					return ResponseEntity.ok(emptyRes);
				} else {
					return ResponseEntity.ok(res);
				}
			}
		}
	}

	@ApiOperation(value = "Filter orders by Payment Mode")
	@GetMapping("/filter/paymentMode/{onlinePaymentMode}")
	public ResponseEntity<?> filterByPaymentMode(@PathVariable("onlinePaymentMode") String onlinePaymentMode,
			@RequestHeader("token") String token) {

		// Validate token
		Users user = tokenService.validateToken(token);
		if (user == null) {
			return ResponseEntity.status(401).body(new GeneralResponse("Unauthorized"));
		} else {
			List<OrderResponse> emptyRes = new ArrayList<OrderResponse>();
			if (user.getUserType().equals(userType.ADMIN)) {
				List<OrderResponse> res = orderService.filterByPaymentMode(onlinePaymentMode, null);
				if (res == null) {
					return ResponseEntity.ok(emptyRes);
				} else {
					return ResponseEntity.ok(res);
				}
			} else {
				List<OrderResponse> res = orderService.filterByPaymentMode(onlinePaymentMode, user.getUserId());
				if (res == null) {
					return ResponseEntity.ok(emptyRes);
				} else {
					return ResponseEntity.ok(res);
				}
			}
		}
	}

	@ApiOperation(value = "Filter orders by Payment Method (COD or ONLINE)")
	@GetMapping("/filter/paymentMethod/{paymentMethod}")
	public ResponseEntity<?> filterByPaymentMethod(@PathVariable("paymentMethod") String paymentMethod,
			@RequestHeader("token") String token) {

		// Validate token
		Users user = tokenService.validateToken(token);
		if (user == null) {
			return ResponseEntity.status(401).body(new GeneralResponse("Unauthorized"));
		} else {
			List<OrderResponse> emptyRes = new ArrayList<OrderResponse>();
			if (user.getUserType().equals(userType.ADMIN)) {
				List<OrderResponse> res = orderService.filterByPaymentMethod(paymentMethod, null);
				if (res == null) {
					return ResponseEntity.ok(emptyRes);
				} else {
					return ResponseEntity.ok(res);
				}
			} else {
				List<OrderResponse> res = orderService.filterByPaymentMethod(paymentMethod, user.getUserId());
				if (res == null) {
					return ResponseEntity.ok(emptyRes);
				} else {
					return ResponseEntity.ok(res);
				}
			}
		}
	}

	@ApiOperation(value = "Filter orders by Transaction Id")
	@GetMapping("/filter/transaction/{transactionId}")
	public ResponseEntity<?> filterByTransactionId(@PathVariable("transactionId") String transactionId,
			@RequestHeader("token") String token) {

		// Validate token
		Users user = tokenService.validateToken(token);
		if (user == null) {
			return ResponseEntity.status(401).body(new GeneralResponse("Unauthorized"));
		} else {
			List<OrderResponse> emptyRes = new ArrayList<OrderResponse>();
			if (user.getUserType().equals(userType.ADMIN)) {
				List<OrderResponse> res = orderService.filterByTransactionId(transactionId, null);
				if (res == null) {
					return ResponseEntity.ok(emptyRes);
				} else {
					return ResponseEntity.ok(res);
				}
			} else {
				List<OrderResponse> res = orderService.filterByTransactionId(transactionId, user.getUserId());
				if (res == null) {
					return ResponseEntity.ok(emptyRes);
				} else {
					return ResponseEntity.ok(res);
				}
			}
		}
	}

	@ApiOperation(value = "Filter orders by Dates")
	@GetMapping("/filter/dates/{startDate}/{endDate}")
	public ResponseEntity<?> filterByDates(@PathVariable("startDate") String startDate,
			@PathVariable("endDate") String endDate, @RequestHeader("token") String token) {

		// Validate token
		Users user = tokenService.validateToken(token);
		if (user == null) {
			return ResponseEntity.status(401).body(new GeneralResponse("Unauthorized"));
		} else {
			List<OrderResponse> emptyRes = new ArrayList<OrderResponse>();
			if (user.getUserType().equals(userType.ADMIN)) {
				List<OrderResponse> res = orderService.filterByDates(LocalDate.parse(startDate),
						LocalDate.parse(endDate), null);
				if (res == null) {
					return ResponseEntity.ok(emptyRes);
				} else {
					return ResponseEntity.ok(res);
				}
			} else {
				List<OrderResponse> res = orderService.filterByDates(LocalDate.parse(startDate),
						LocalDate.parse(endDate), user.getUserId());
				if (res == null) {
					return ResponseEntity.ok(emptyRes);
				} else {
					return ResponseEntity.ok(res);
				}
			}
		}
	}

	@ApiOperation(value = "Map order to vendor")
	@GetMapping("/map/{orderId}/{vendorId}")
	public ResponseEntity<?> mapOrderToVendor(@RequestHeader("token") String token,
			@PathVariable("orderId") Long orderId, @PathVariable("vendorId") Long vendorId) {

		// Validate token
		Users user = tokenService.validateToken(token);
		if (user == null) {
			return ResponseEntity.status(401).body(new GeneralResponse("Unauthorized"));
		}

		OrderResponse res = orderService.mapOrderToVendor(orderId, vendorId);
		if (res == null) {
			return ResponseEntity.status(500).body(new GeneralResponse("Failed"));
		} else {
			return ResponseEntity.ok(res);
		}
	}

	@ApiOperation("Get all orders from the vendor")
	@GetMapping("/vendor/allOrders/{vendorId}")
	public ResponseEntity<?> getAllVendorOrders(@RequestHeader("token") String token,
			@PathVariable("vendorId") Long vendorId) {

		// Validate token
		DeliveryUser user = tokenService.validateDeliveryToken(token);
		if (user == null) {
			return ResponseEntity.status(401).body(new GeneralResponse("Unauthorized"));
		}

		List<OrderResponse> res = orderService.getOrdersOfVendor(vendorId);
		if (res == null) {
			return ResponseEntity.status(500).body(new GeneralResponse("Failed"));
		} else {
			return ResponseEntity.ok(res);
		}
	}

	@ApiOperation("Get open orders for the vendor")
	@GetMapping("/vendor/orders/open/{vendorId}")
	public ResponseEntity<?> getOpenVendorOrders(@RequestHeader("token") String token,
			@PathVariable("vendorId") Long vendorId) {

		// Validate token
		DeliveryUser user = tokenService.validateDeliveryToken(token);
		if (user == null) {
			return ResponseEntity.status(401).body(new GeneralResponse("Unauthorized"));
		}
		List<OrderResponse> res = orderService.getVendorOrderByStatus(vendorId, OrderStatus.CONFIRMED, null);
		if (res == null) {
			return ResponseEntity.status(500).body(new GeneralResponse("Failed"));
		} else {
			return ResponseEntity.ok(res);
		}
	}

	@ApiOperation("Get completed orders for the vendor")
	@GetMapping("/vendor/orders/closed/{vendorId}")
	public ResponseEntity<?> getClosedVendorOrders(@RequestHeader("token") String token,
			@PathVariable("vendorId") Long vendorId) {

		// Validate token
		DeliveryUser user = tokenService.validateDeliveryToken(token);
		if (user == null) {
			return ResponseEntity.status(401).body(new GeneralResponse("Unauthorized"));
		}
		List<OrderResponse> res = orderService.getVendorOrderByStatus(vendorId, OrderStatus.COMPLETED,
				OrderStatus.DELIVERED);
		if (res == null) {
			return ResponseEntity.status(500).body(new GeneralResponse("Failed"));
		} else {
			return ResponseEntity.ok(res);
		}
	}

	@ApiOperation(value = "Get Order of one day")
	@GetMapping("/sales/today")
	public ResponseEntity<?> salesToday(@RequestHeader("token") String token) {

		// Validate token
		Users user = tokenService.validateToken(token);
		if (user == null) {
			return ResponseEntity.status(401).body(new GeneralResponse("Unauthorized"));
		}
		return ResponseEntity.ok(orderService.getDailySales());
	}

	@ApiOperation(value = "Get Order of one month")
	@GetMapping("/sales/monthly")
	public ResponseEntity<?> salesMonthly(@RequestHeader("token") String token) {

		// Validate token
		Users user = tokenService.validateToken(token);
		if (user == null) {
			return ResponseEntity.status(401).body(new GeneralResponse("Unauthorized"));
		}
		return ResponseEntity.ok(orderService.getMonthlySales());
	}

	@ApiOperation(value = "Get Order of one year")
	@GetMapping("/sales/yearly")
	public ResponseEntity<?> salesYearly(@RequestHeader("token") String token) {

		// Validate token
		Users user = tokenService.validateToken(token);
		if (user == null) {
			return ResponseEntity.status(401).body(new GeneralResponse("Unauthorized"));
		}
		return ResponseEntity.ok(orderService.getYearlySales());
	}

	@ApiOperation(value = "Get Data for drawing charts")
	@GetMapping("/chart")
	public ResponseEntity<?> filterByTransactionId(@RequestHeader("token") String token) {

		// Validate token
		Users user = tokenService.validateToken(token);
		if (user == null) {
			return ResponseEntity.status(401).body(new GeneralResponse("Unauthorized"));
		} else {
			if (user.getUserType().equals(userType.ADMIN)) {
				return ResponseEntity.ok(orderService.getChartsData(null));
			} else if (user.getUserType().equals(userType.VENDOR)) {
				return ResponseEntity.ok(orderService.getChartsData(user.getUserId()));
			} else {
				return ResponseEntity.status(401).body(new GeneralResponse("Unauthorized"));
			}
		}
	}

	@ApiOperation(value = "Create new feedback for order")
	@PostMapping("/feedback")
	public ResponseEntity<?> createFeedback(@RequestBody FeedbackDTO req, @RequestHeader("token") String token) {
		// Validate token
		Users user = tokenService.validateToken(token);
		if (user == null) {
			return ResponseEntity.status(401).body(new GeneralResponse("Unauthorized"));
		}
		OrderResponse order = orderService.viewOrder(req.getOrderId(), user);
		if (order != null) {
			if (order.getOrderStatus().equals(OrderStatus.COMPLETED)) {
				Feedback feedback = feedbackService.createFeedback(req);
				if (feedback == null) {
					return ResponseEntity.status(500).body(new GeneralResponse("Failure"));
				}
				return ResponseEntity.ok(feedback);
			} else {
				return ResponseEntity.badRequest().body(new GeneralResponse("Invalid Order"));
			}
		} else {
			return ResponseEntity.badRequest().body(new GeneralResponse("Invalid Order"));
		}

	}

	@ApiOperation(value = "View feedback for any order")
	@GetMapping("/feedback/view/{orderId}")
	public ResponseEntity<?> viewFeedback(@PathVariable("orderId") Long orderId, @RequestHeader("token") String token) {
		// Validate token
		Users user = tokenService.validateToken(token);
		if (user == null) {
			return ResponseEntity.status(401).body(new GeneralResponse("Unauthorized"));
		}
		OrderResponse order = orderService.viewOrder(orderId, user);
		if (order != null) {
			if (order.getOrderStatus().equals(OrderStatus.COMPLETED)) {
				Feedback feedback = feedbackService.getFeedback(orderId);
				if (feedback == null) {
					return ResponseEntity.badRequest().body(new GeneralResponse("Invalid Order"));
				} else {
					return ResponseEntity.ok(feedback);
				}
			} else {
				return ResponseEntity.status(500).body(new GeneralResponse("Failure"));
			}
		} else {
			return ResponseEntity.badRequest().body(new GeneralResponse("Invalid Order"));
		}

	}
}
