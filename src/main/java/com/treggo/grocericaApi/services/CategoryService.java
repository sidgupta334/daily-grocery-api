package com.treggo.grocericaApi.services;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.treggo.grocericaApi.entities.Category;
import com.treggo.grocericaApi.entities.ImgMaster;
import com.treggo.grocericaApi.entities.Product;
import com.treggo.grocericaApi.repositories.CategoryRepository;
import com.treggo.grocericaApi.repositories.ProductRepository;
import com.treggo.grocericaApi.requests.NewCategoryDTO;
import com.treggo.grocericaApi.responses.CategoryResponse;

@Service
public class CategoryService {

	@Autowired
	private CategoryRepository repo;

	@Autowired
	private ImageService imgService;
	
	@Autowired
	private ProductRepository pRepo;

	@Value("${app.server}")
	private String serverAddress;

	Logger logger = LoggerFactory.getLogger(CategoryService.class);

	public CategoryResponse createCategory(NewCategoryDTO req) {
		
		if(req.getCategoryId() != null) {
			Category c = repo.findByCategoryId(req.getCategoryId());
			if(req.getCategoryName() != null) {
				c.setCategoryName(req.getCategoryName());
			}
			
			repo.save(c);
			return new CategoryResponse(c.getCategoryId(),
					serverAddress + "images/download/" + c.getCategoryImage().getImgId(), c.getCategoryName());
			
		}
		ImgMaster img = imgService.getImage(req.getImgId());
		if (img == null) {
			return null;
		}

		Category category = new Category();
		category.setCategoryImage(img);
		category.setCategoryName(req.getCategoryName());
		category.setCreated_on(LocalDate.now());

		try {
			repo.save(category);
			return new CategoryResponse(category.getCategoryId(),
					serverAddress + "images/download/" + category.getCategoryImage().getImgId(), category.getCategoryName());
		} catch (Exception e) {
			logger.error("Failed to run: " + e);
			return null;
		}
	}

	public boolean deleteCategory(Long categoryId) {
		try {
			Category category = repo.findByCategoryId(categoryId);
			List<Product> products = pRepo.fetchByCategoryId(categoryId);
			for(Product pr : products) {
				imgService.deleteImage(pr.getProductImage());
				pRepo.delete(pr);
			}
			imgService.deleteImage(category.getCategoryImage());
			repo.delete(category);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public CategoryResponse getSingleCategory(Long categoryId) {
		Category category = repo.findByCategoryId(categoryId);
		return new CategoryResponse(category.getCategoryId(),
				serverAddress + "images/download/" + category.getCategoryImage().getImgId(), category.getCategoryName());
	}
	
	public List<CategoryResponse> findAllCategories() {
		List<CategoryResponse> res = new ArrayList<>();
		try {
			List<Category> categories = repo.findAll();
			for(Category c : categories) {
				CategoryResponse r = new CategoryResponse();
				r.setCategoryId(c.getCategoryId());
				r.setCategoryName(c.getCategoryName());
				r.setUrl(serverAddress + "images/download/" + c.getCategoryImage().getImgId());
				res.add(r);
			}
			return res;
		}
		catch(Exception e) {
			logger.error("Failed to run: " + e);
			return null;
		}
	}
}
