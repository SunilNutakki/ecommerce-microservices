package com.nt.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.nt.dto.CategoryWithProductsDTO;
import com.nt.dto.ProductDTO;
import com.nt.dto.genderDTO;
import com.nt.model.product;
import com.nt.responseDTO.ProductItemResponse;

public interface IProductService {
	public List<ProductDTO> getAllProducts();
	

	public Optional<ProductDTO> getProductById(String pid);
	
	public String saveproductDtls(ProductDTO pdto);
	
	public String updateProductDtlsByProductId(String pid,ProductDTO pdto);
	
	public String deleteProductDtlsByProductId(String pid);
	
	
    List<ProductDTO> getProductsByCategoryId(String categoryId);

    CategoryWithProductsDTO getGroupProductsByCategoryId(String categoryId);


	List<CategoryWithProductsDTO> getAllCategoriesWithProducts();
	List <genderDTO>getAllGenderByCategories();
	List <genderDTO> getAllGender();


	List<CategoryWithProductsDTO> getCategoryHierarchy();
	List<CategoryWithProductsDTO> getParentCategoryCombo();
    List<CategoryWithProductsDTO> getProductCategoriesByParentId(String parentCategoryId,String deptId);
    
    List<ProductDTO> getProductsByCategoryIds(String categoryId);


	public Page<ProductDTO> getProductsByCategoryPaginationIds(String categoryId, Pageable pageable);
	List<ProductDTO> getProductsByCategoryIdsSortBy(String categoryId,boolean ascOrder,String... sortBy);

	ProductItemResponse getProductItemById(String productItemId);
}
