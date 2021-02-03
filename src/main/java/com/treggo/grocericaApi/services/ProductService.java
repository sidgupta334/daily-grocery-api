package com.treggo.grocericaApi.services;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.treggo.grocericaApi.entities.Category;
import com.treggo.grocericaApi.entities.ImgMaster;
import com.treggo.grocericaApi.entities.Product;
import com.treggo.grocericaApi.entities.ProductBckp;
import com.treggo.grocericaApi.entities.Users;
import com.treggo.grocericaApi.repositories.CategoryRepository;
import com.treggo.grocericaApi.repositories.ProductBckpRepository;
import com.treggo.grocericaApi.repositories.ProductRepository;
import com.treggo.grocericaApi.requests.NewProductDTO;
import com.treggo.grocericaApi.responses.ProductResponse;

@Service
public class ProductService {

	@Autowired
	private ProductRepository repo;

	@Autowired
	private ProductBckpRepository pRepo;

	@Autowired
	private ImageService imgService;

	@Autowired
	private UserServices userService;
	
	@Autowired
	private CategoryRepository catRepo;
	
	Logger logger = LoggerFactory.getLogger(ProductService.class);

	@Value("${app.server}")
	private String serverAddress;

	public ProductResponse createProduct(NewProductDTO req) {
		
		if(req.getProductId() != null) {
			Product pr = repo.findByProductId(req.getProductId());
			pr.setBrand(req.getBrand());
			pr.setCategory(catRepo.findByCategoryId(req.getCategoryId()));
			pr.setNewPrice(req.getNewPrice());
			pr.setOldPrice(req.getOldPrice());
			pr.setProductDescription(req.getProductDescription());
			pr.setProductName(req.getProductName());
			pr.setVendorId(req.getVendorId());
			if(req.getImgId() != null) {
				try {
					imgService.deleteImage(pr.getProductImage());
				} catch (Exception e) {
					logger.error("Failed to run: " + e);
					return null;
				}
				pr.setProductImage(imgService.getImage(req.getImgId()));
			}
			repo.save(pr);
			ProductResponse resp = new ProductResponse();
			BeanUtils.copyProperties(pr, resp);
			resp.setCategoryId(pr.getCategory().getCategoryId());
			resp.setCategoryName(pr.getCategory().getCategoryName());
			resp.setVendorId(pr.getVendorId());
			Users vendor = userService.findUserById(pr.getVendorId());
			resp.setVendor(vendor.getFullName());
			resp.setUrl(serverAddress + "images/download/" + pr.getProductImage().getImgId());
			return resp;
			
		}

		ImgMaster img = imgService.getImage(req.getImgId());
		Category cat = catRepo.findByCategoryId(req.getCategoryId());

		if (img == null || cat == null) {
			return null;
		}

		Product product = new Product();
		ProductBckp pr = new ProductBckp();
		BeanUtils.copyProperties(req, product);
		product.setProductImage(img);
		product.setCategory(cat);
		product.setCreated_on(LocalDate.now());
		product.setAvailable(true);
		product.setVendorId(req.getVendorId());
		BeanUtils.copyProperties(product, pr);
		pr.setCategoryId(req.getCategoryId());
		pr.setVendorId(req.getVendorId());
		Users vendor = userService.findUserById(pr.getVendorId());
		pr.setVendor(vendor.getFullName());
		pr.setCategoryName(cat.getCategoryName());
		try {
			repo.save(product);
			pr.setProductId(product.getProductId());
			pRepo.save(pr);
			ProductResponse resp = new ProductResponse();
			BeanUtils.copyProperties(product, resp);
			resp.setCategoryId(product.getCategory().getCategoryId());
			resp.setCategoryName(product.getCategory().getCategoryName());
			resp.setVendorId(product.getVendorId());
			Users vend = userService.findUserById(pr.getVendorId());
			resp.setVendor(vend.getFullName());
			resp.setUrl(serverAddress + "images/download/" + product.getProductImage().getImgId());
			return resp;
		} catch (Exception e) {
			logger.error("Failed to run: " + e);
			return null;
		}
	}

	public boolean deleteProduct(Long productId) {
		try {
			Product p = repo.findByProductId(productId);
			imgService.deleteImage(p.getProductImage());
			repo.delete(p);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public ProductResponse getSingleProduct(Long productId) {
		Product p = repo.findByProductId(productId);
		ProductResponse res = new ProductResponse();
		BeanUtils.copyProperties(p, res);
		res.setCategoryId(p.getCategory().getCategoryId());
		res.setCategoryName(p.getCategory().getCategoryName());
		res.setVendorId(p.getVendorId());
		Users vendor = userService.findUserById(p.getVendorId());
		res.setVendor(vendor.getFullName());
		res.setUrl(serverAddress + "images/download/" + p.getProductImage().getImgId());
		return res;
	}
	
	public Product findProduct(Long productId) {
		return repo.findByProductId(productId);
	}

	public List<ProductResponse> getAllProducts() {
		List<Product> pList = repo.findAll();
		List<ProductResponse> res = new ArrayList<ProductResponse>();
		for (Product p : pList) {
			ProductResponse r = new ProductResponse();
			BeanUtils.copyProperties(p, r);
			r.setCategoryId(p.getCategory().getCategoryId());
			r.setCategoryName(p.getCategory().getCategoryName());
			r.setVendorId(p.getVendorId());
			Users vendor = userService.findUserById(r.getVendorId());
			r.setVendor(vendor.getFullName());
			r.setUrl(serverAddress + "images/download/" + p.getProductImage().getImgId());
			res.add(r);
		}
		return res;
	}

	public List<ProductResponse> getProductsByCategory(Long categoryId) {
		List<Product> pList = repo.fetchByCategoryId(categoryId);
		List<ProductResponse> res = new ArrayList<ProductResponse>();
		for (Product p : pList) {
			ProductResponse r = new ProductResponse();
			BeanUtils.copyProperties(p, r);
			r.setCategoryId(p.getCategory().getCategoryId());
			r.setCategoryName(p.getCategory().getCategoryName());
			r.setVendorId(p.getVendorId());
			Users vendor = userService.findUserById(r.getVendorId());
			r.setVendor(vendor.getFullName());
			r.setUrl(serverAddress + "images/download/" + p.getProductImage().getImgId());
			res.add(r);
		}
		return res;
	}

	public List<ProductResponse> seachProducts(String productName) {
		List<Product> pList = repo.searchProducts(productName);
		List<ProductResponse> res = new ArrayList<ProductResponse>();
		for (Product p : pList) {
			ProductResponse r = new ProductResponse();
			BeanUtils.copyProperties(p, r);
			r.setCategoryId(p.getCategory().getCategoryId());
			r.setCategoryName(p.getCategory().getCategoryName());
			r.setVendorId(p.getVendorId());
			Users vendor = userService.findUserById(r.getVendorId());
			r.setVendor(vendor.getFullName());
			r.setUrl(serverAddress + "images/download/" + p.getProductImage().getImgId());
			res.add(r);
		}
		return res;
	}
	
	public boolean setAvailability(Long productId, boolean status) {
		try {
			Product pr = repo.findByProductId(productId);
			pr.setAvailable(status);
			repo.save(pr);
			return true;
		} catch(Exception e) {
			logger.error("Failed to run: " + e);
			return false;
		}
	}

}
