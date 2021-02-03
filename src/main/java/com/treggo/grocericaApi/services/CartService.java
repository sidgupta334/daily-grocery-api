package com.treggo.grocericaApi.services;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.treggo.grocericaApi.entities.Cart;
import com.treggo.grocericaApi.entities.Product;
import com.treggo.grocericaApi.entities.ProductBckp;
import com.treggo.grocericaApi.entities.Users;
import com.treggo.grocericaApi.repositories.CartRepository;
import com.treggo.grocericaApi.repositories.ProductBckpRepository;
import com.treggo.grocericaApi.repositories.ProductRepository;
import com.treggo.grocericaApi.responses.CartResponse;
import com.treggo.grocericaApi.responses.ProductCartResponse;

@Service
public class CartService {

	@Autowired
	private CartRepository repo;

	@Autowired
	private ProductRepository pRepo;

	@Autowired
	private ProductBckpRepository prRepo;

	@Value("${app.server}")
	private String serverAddress;
	
	Logger logger = LoggerFactory.getLogger(CartService.class);


	public CartResponse addToCart(Long productId, Users user) {

		// Check if cart existed
		try {
			Cart cart = repo.fetchByUserId(user.getUserId());

			// Create new cart
			if (cart == null || cart.getProductIds() == null) {
				Cart newCart = new Cart();
				if (cart != null) {
					newCart.setCartId(cart.getCartId());
				}
				newCart.setProductIds(Long.toString(productId));
				newCart.setProductQuantities(Long.toString(1));
				newCart.setNetQuantity(new Long(1));
				newCart.setNetTotal(getProductPrice(pRepo.findByProductId(productId)));
				newCart.setUser(user);
				newCart.setCreated_on(LocalDate.now());
				repo.save(newCart);
				return mapToCart(newCart);
			} else {
				cart = addProduct(cart, productId);
				return mapToCart(cart);
			}

		} catch (Exception e) {
			logger.error("Failed to run: " + e);
			return null;
		}
	}

	public CartResponse removeFromCart(Long productId, Users user) {
		// Check if cart existed
		try {
			Cart cart = repo.fetchByUserId(user.getUserId());
			if (cart == null) {
				return null;
			}

			cart = removeProduct(cart, productId);
			return mapToCart(cart);

		} catch (Exception e) {
			logger.error("Failed to run: " + e);
			return null;
		}
	}

	public CartResponse emptyCart(Users user) {

		// Check if cart existed
		try {
			Cart cart = repo.fetchByUserId(user.getUserId());
			if (cart == null) {
				return null;
			}

			cart.setNetQuantity(new Long(0));
			cart.setNetTotal(new Long(0));
			cart.setProductIds(null);
			cart.setProductQuantities(null);
			repo.save(cart);
			return mapToCart(cart);

		} catch (Exception e) {
			logger.error("Failed to run: " + e);
			return null;
		}
	}

	public CartResponse viewCart(Users user) {
		// Check if cart existed
		try {
			Cart cart = repo.fetchByUserId(user.getUserId());
			if (cart == null) {
				CartResponse res = new CartResponse();
				res.setNetQuantity(new Long(0));
				res.setNetTotal(new Long(0));
				return res;
			}

			return mapToCart(cart);

		} catch (Exception e) {
			logger.error("Failed to run: " + e);
			return null;
		}
	}
	 
	public Cart getCartById(Long cartId) {
		return repo.findByCartId(cartId);
	}

	private Long calculateNetTotal(Cart cart) {

		Long netTotal = new Long(0);
		if(cart.getProductIds().equals("")) {
			return netTotal;
		}
		String[] productIds;
		if(cart.getProductIds().equals("")) {
			productIds = new String[0];
		} else {
			productIds = cart.getProductIds().split("\\|");
		}
		String[] productQuantities = cart.getProductQuantities().split("\\|");
		for (int i = 0; i < productIds.length; i++) {
			Product p = pRepo.findByProductId(Long.parseLong(productIds[i]));
			if(p == null) {
				ProductBckp pr = prRepo.findByProductId(Long.parseLong(productIds[i]));
				netTotal = netTotal + (getProductPriceBckp(pr) * Long.parseLong(productQuantities[i]));
			}
			else {
				netTotal = netTotal + (getProductPrice(p) * Long.parseLong(productQuantities[i]));
			}
		}

		return netTotal;
	}

	private Long calculateNetQuantity(Cart cart) {
		Long netQuantity = new Long(0);
		String[] productIds;
		if(cart.getProductIds().equals("")) {
			productIds = new String[0];
		} else {
			productIds = cart.getProductIds().split("\\|");
		}
		String[] productQuantities = cart.getProductQuantities().split("\\|");
		for (int i = 0; i < productIds.length; i++) {
			netQuantity = netQuantity + Long.parseLong(productQuantities[i]);
		}
		return netQuantity;
	}

	public CartResponse mapToCart(Cart cart) {
		CartResponse res = new CartResponse();
		try {
			res.setCartId(cart.getCartId());
			List<ProductCartResponse> products = new ArrayList<ProductCartResponse>();
			if (cart.getProductIds() == null) {
				res.setProducts(products);
				res.setNetQuantity(new Long(0));
				res.setNetTotal(new Long(0));
				return res;
			}
			String[] productIds;
			if(cart.getProductIds().equals("")) {
				productIds = new String[0];
			} else {
				productIds = cart.getProductIds().split("\\|");	
			}
			String[] productQuantities = cart.getProductQuantities().split("\\|");

			for (int i = 0; i < productIds.length; i++) {
				ProductCartResponse pr = new ProductCartResponse();
				Product p = pRepo.findByProductId(Long.parseLong(productIds[i]));
				if (p == null) {
					ProductBckp bckp = prRepo.findByProductId(Long.parseLong(productIds[i]));
					BeanUtils.copyProperties(bckp, pr);
					pr.setCategoryId(bckp.getCategoryId());
					pr.setCategoryName(bckp.getCategoryName());
					pr.setUrl(null);
					pr.setQuantity(Long.parseLong(productQuantities[i]));
				} else {
					BeanUtils.copyProperties(p, pr);
					pr.setCategoryId(p.getCategory().getCategoryId());
					pr.setCategoryName(p.getCategory().getCategoryName());
					pr.setUrl(serverAddress + "images/download/" + p.getProductImage().getImgId());
					pr.setQuantity(Long.parseLong(productQuantities[i]));
				}
				products.add(pr);
			}

			res.setProducts(products);
			res.setNetQuantity(calculateNetQuantity(cart));
			res.setNetTotal(calculateNetTotal(cart));
			return res;
		} catch (Exception e) {
			logger.error("Failed to run: " + e);
			return null;
		}

	}

	private Cart addProduct(Cart cart, Long productId) {
		
		String[] productIds;
		String[] productQuantities;
		if(cart.getProductIds().equals("")) {
			productIds = new String[0];
			productQuantities = new String[0];
		} else {
			productIds = cart.getProductIds().split("\\|");
			productQuantities = cart.getProductQuantities().split("\\|");
		}
		
		try {
			boolean isExist = false;
			for (int i = 0; i < productIds.length; i++) {
				if (productId == Long.parseLong(productIds[i])) {
					productQuantities[i] = Long.toString(Long.parseLong(productQuantities[i]) + 1);
					isExist = true;
					break;
				}
			}

			if (productIds.length == 1) {
				cart.setProductIds(productIds[0]);
				cart.setProductQuantities(productQuantities[0]);
			} else if(productIds.length == 0) {
				cart.setProductIds("");
				cart.setProductQuantities("");
			} else {
				cart.setProductIds(String.join("|", productIds));
				cart.setProductQuantities(String.join("|", productQuantities));

			}

			if (!isExist) {
				if(productIds.length == 0) {
					cart.setProductIds(cart.getProductIds().concat(productId.toString()));
					cart.setProductQuantities(cart.getProductQuantities().concat("1"));
				} else {
					cart.setProductIds(cart.getProductIds().concat("|" + productId));
					cart.setProductQuantities(cart.getProductQuantities().concat("|" + "1"));
				}
			}

			cart.setNetQuantity(calculateNetQuantity(cart));
			cart.setNetTotal(calculateNetTotal(cart));
			repo.save(cart);
			return cart;

		} catch (Exception e) {
			return null;
		}
	}

	private Cart removeProduct(Cart cart, Long productId) {
		String[] productIds;
		String[] productQuantities;
		if(cart.getProductIds().equals("")) {
			productIds = new String[0];
			productQuantities = new String[0];
		} else {
			productIds = cart.getProductIds().split("\\|");
			productQuantities = cart.getProductQuantities().split("\\|");
		}
		
		if (productIds.length == 0) {
			return null;
		}
		try {
			boolean isExist = false;
			for (int i = 0; i < productIds.length; i++) {
				if (productId == Long.parseLong(productIds[i])) {
					if (Long.parseLong(productQuantities[i]) == 1) {
						productIds = ArrayUtils.remove(productIds, i);
						productQuantities = ArrayUtils.remove(productQuantities, i);
					} else {
						productQuantities[i] = Long.toString(Long.parseLong(productQuantities[i]) - 1);
					}
					isExist = true;
					break;
				}
			}
			if (isExist) {
				cart.setProductIds(String.join("|", productIds));
				cart.setProductQuantities(String.join("|", productQuantities));
				cart.setNetQuantity(calculateNetQuantity(cart));
				cart.setNetTotal(calculateNetTotal(cart));
				repo.save(cart);
				return cart;
			}
			return null;
		} catch (Exception e) {
			logger.error("Failed to run: " + e);
			return null;
		}
	}

	private Long getProductPriceBckp(ProductBckp pr) {
		int price = pr.getNewPrice();
		return new Long(price);
	}
	
	private Long getProductPrice(Product pr) {
		int price = pr.getNewPrice();
		return new Long(price);
	}
}
