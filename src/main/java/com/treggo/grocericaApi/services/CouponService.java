package com.treggo.grocericaApi.services;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.treggo.grocericaApi.entities.Cart;
import com.treggo.grocericaApi.entities.Coupon;
import com.treggo.grocericaApi.entities.Product;
import com.treggo.grocericaApi.entities.Users;
import com.treggo.grocericaApi.enums.userType;
import com.treggo.grocericaApi.repositories.CouponRepository;
import com.treggo.grocericaApi.repositories.ProductRepository;
import com.treggo.grocericaApi.repositories.UsersRepository;
import com.treggo.grocericaApi.requests.ApplyCouponDTO;
import com.treggo.grocericaApi.responses.CartResponse;
import com.treggo.grocericaApi.responses.CouponResponse;
import com.treggo.grocericaApi.responses.ProductCartResponse;

@Service
public class CouponService {

	@Autowired
	private CouponRepository repo;
	
	@Autowired
	private UsersRepository uRepo;
	
	@Autowired
	private CartService cartService;
	
	@Autowired
	private ProductRepository pRepo;

	Logger logger = LoggerFactory.getLogger(CouponService.class);

	
	public Coupon createCoupon(Coupon coupon) {
		Coupon c = repo.findByCouponId(coupon.getCouponId());
		if (c != null) {
			return null;
		}

		if (coupon.getCouponName() == null || coupon.getMaxDiscount() == null || coupon.getVendorId() == null) {
			return null;
		}

		coupon.setCreated_on(LocalDate.now());
		try {
			Users user = uRepo.fetchByUserId(coupon.getVendorId());
			if(user == null) {
				return null;
			}
			repo.save(coupon);
			return coupon;
		} catch (Exception e) {
			return null;
		}
	}

	public boolean deleteCoupon(Long couponId) {
		try {
			repo.deleteById(couponId);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public List<Coupon> getAllCoupons(Users user) {
		if(user.getUserType().equals(userType.ADMIN)) {
			return repo.findAll();
		} else {
			return repo.findByVendorId(user.getUserId());
		}
	}
	
	public Coupon findCoupon(String name) {
		return repo.findByCouponName(name);
	}

	public CouponResponse applyCoupon(ApplyCouponDTO req) {
		Long discountAmount = null;
		Long amountToApply = new Long(0);
		Long amountToLeave = new Long(0);
		
		try {
			Cart c = cartService.getCartById(req.getCartId());
			Coupon coupon = repo.findByCouponName(req.getCouponName());
			Users user = c.getUser();
			CartResponse cart = cartService.viewCart(user);
			List<Product> productsOfVendor = new ArrayList<Product>();
			for(ProductCartResponse pr : cart.getProducts()) {
				Product p = pRepo.findByProductId(pr.getProductId());
				if(p.getVendorId() == coupon.getVendorId()) {
					productsOfVendor.add(p);
					amountToApply += (pr.getQuantity() * pr.getNewPrice());
				} else {
					amountToLeave += (pr.getQuantity() * pr.getNewPrice());
				}
			}
			if (coupon == null) {
				return null;
			}

			if (coupon.getPercentageDiscount() > 0) {
				double percentageAmount = ((double) coupon.getPercentageDiscount() / 100) * amountToApply;
				if (percentageAmount > coupon.getMaxDiscount()) {
					discountAmount = amountToApply - coupon.getMaxDiscount();
				} else {
					discountAmount = amountToApply - (long) percentageAmount;
				}
			} else {
				if((amountToApply + amountToLeave) < 300) {
					discountAmount = amountToApply;
				} else {
					discountAmount = amountToApply - coupon.getMaxDiscount();
				}
			}
			
			CouponResponse res = new CouponResponse();
			res.setCouponName(req.getCouponName());
			res.setOldPrice(amountToApply + amountToLeave);
			res.setUpdatedPrice(discountAmount + amountToLeave);
			return res;
		}

		catch (Exception e) {
			logger.error("Failed to run: " + e);
			return null;
		}
	}
}
