package com.nt.restController;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nt.dto.CategoryWithProductsDTO;
import com.nt.dto.ProductDTO;
import com.nt.dto.genderDTO;
import com.nt.model.product;
import com.nt.responseDTO.ProductItemResponse;
import com.nt.service.IProductService;

import io.swagger.v3.oas.annotations.tags.Tag;
@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/product-api")

@Tag(
	    name = "Prodct Management",
	    description = "APIs for creating, managing and checking out Products"
	)
public class productController {
	@Autowired
	private IProductService service;
	
	 @GetMapping("/products")
	    public ResponseEntity<List<ProductDTO>> getAllProducts() {
	        List<ProductDTO> list = service.getAllProducts();
	        return ResponseEntity.ok(list);
	    }
	 
	 @GetMapping("/productById/{id}")
	 public ResponseEntity<ProductDTO> getProductById(@PathVariable String id) {
	     return service.getProductById(id)
	                   .map(ResponseEntity::ok)
	                   .orElse(ResponseEntity.notFound().build());
	 }

	 @PostMapping("/savePro")
	    public ResponseEntity<String> saveProduct(@RequestBody ProductDTO productDTO) {
	        String result = service.saveproductDtls(productDTO);
	        return ResponseEntity.ok(result);
	    }
	 
	 @PutMapping("updateProduct/{id}")
	 public ResponseEntity<String> updateProduct(
	         @PathVariable("id") String productId,
	         @RequestBody ProductDTO productDTO) {

	     String result = service.updateProductDtlsByProductId(productId, productDTO);
	     return ResponseEntity.ok(result);
	 }
	 
	 @DeleteMapping("deleteProduct/{id}")
	 public ResponseEntity<String> deleteProduct(@PathVariable("id") String pid) {
	     String msg = service.deleteProductDtlsByProductId(pid);
	     return ResponseEntity.ok(msg);
	 }
	 
	 @GetMapping("/productByCategoryId")
	    public ResponseEntity<List<ProductDTO>> getProductsByCategory(@RequestParam(required = false) String categoryId) {
	        if (categoryId != null) {
	            List<ProductDTO> products = service.getProductsByCategoryId(categoryId);
	            return ResponseEntity.ok(products);
	        } else {
	            return ResponseEntity.badRequest().build(); // You can extend this to return all products too
	        }
	    }
	 
	 @GetMapping("/productCatid")
	    public ResponseEntity<CategoryWithProductsDTO> getGroupProductsByCategoryId(@RequestParam String categoryId) {
		 CategoryWithProductsDTO dto = service.getGroupProductsByCategoryId(categoryId);

	        if (dto == null) {
	            return ResponseEntity.notFound().build(); // return 404 if category has no products
	        }
	        return ResponseEntity.ok(dto);
	    }
	 
	    @GetMapping("/all-products")
	    public ResponseEntity<List<CategoryWithProductsDTO>> getGroupedProducts() {
	        List<CategoryWithProductsDTO> response = service.getAllCategoriesWithProducts();
	        return ResponseEntity.ok(response);
	    }
	    
	    @GetMapping("/categories")
	    public List<genderDTO> getAllGenderWithCategories() {
	        return service.getAllGenderByCategories();
	    }
	    
	    @GetMapping("/allgender")
	    public List<genderDTO> getAllGender() {
	        return service.getAllGender();
	    }
	    
	    @GetMapping("/hierarchy")
	    public List<CategoryWithProductsDTO> getCategoryHierarchy() {
	        return service.getCategoryHierarchy();
	    }
	    
	    
	    @GetMapping("/parent-categories")
	    public List<CategoryWithProductsDTO> getParentCategoryCombo() {
	        return service.getParentCategoryCombo();
	    }
		/*
		 * @GetMapping("/parent/{parentId}") public List<CategoryWithProductsDTO>
		 * getCategoriesByParentId(@PathVariable String parentId) { return
		 * service.getProductCategoriesByParentId(parentId); }
		 */
	    
	    @GetMapping("/parent")
	    public List<CategoryWithProductsDTO> getCategoriesByParentId(@RequestParam String parentId,@RequestParam String deptId) {
	        return service.getProductCategoriesByParentId(parentId,deptId);
	    }
	    
	    @GetMapping("/getProducts")
	    public List<ProductDTO> getProductsByCategoryIds(@RequestParam String categoryId){	    	
	    	return service.getProductsByCategoryIds(categoryId);
	    	
	    }
	  //http://localhost:8086/BootProj09-RestAPI-Ecommerce/product-api/getProdByPagination?categoryId=PC000008&page=0&size=5&sort=productId,desc  
	    @GetMapping("/getProdByPagination")
	    public Page<ProductDTO> getProductsByCategoryIds(
	            @RequestParam String categoryId,
	            Pageable pageable) {

	        return service.getProductsByCategoryPaginationIds(categoryId, pageable);
	    }
	//http://localhost:8086/BootProj09-RestAPI-Ecommerce/product-api/getProductsSortBy?categoryId=PC000008&ascOrder=false&sortBy=productId    
	    @GetMapping("/getProductsSortBy")
	    public List<ProductDTO> getProductsByCategoryIds(
	            @RequestParam String categoryId,
	            @RequestParam(defaultValue = "true") boolean ascOrder,
	            @RequestParam(defaultValue = "productId") String... sortBy) {

	        return service.getProductsByCategoryIdsSortBy(categoryId, ascOrder, sortBy);
	    }
	    
	    @GetMapping("/productItemById/{id}")
	    public ResponseEntity<ProductItemResponse> getProductItemById(
	            @PathVariable("id") String id) {

	        ProductItemResponse response =
	        		service.getProductItemById(id);

	        return ResponseEntity.ok(response);
	    }
	    
        
}
