package com.nt.service;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nt.dto.CategoryWithProductsDTO;
import com.nt.dto.ImageDTO;
import com.nt.dto.ProductDTO;
import com.nt.dto.genderDTO;
import com.nt.model.ParentCategory;
import com.nt.model.ProductCategory;
import com.nt.model.ProductGender;
import com.nt.model.ProductImage;
import com.nt.model.ProductItem;
import com.nt.model.product;
import com.nt.repository.ICustomProductRepo;
import com.nt.repository.ImageRepository;
import com.nt.repository.ParentCategoryRepository;
import com.nt.repository.ProductCategoryRepository;
import com.nt.repository.ProductItemRepository;
import com.nt.repository.productGenderRepository;
import com.nt.responseDTO.ProductItemResponse;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class CusomProductServiceImpl implements IProductService {
	@Value("${app.image.base-url}")
	private String imageBaseUrl;

    @Autowired
    private ICustomProductRepo repo;
   
    @Autowired
    private ProductCategoryRepository categoryRepo;
    
    @Autowired
    private productGenderRepository genderRepo;
    
    @Autowired
    private ParentCategoryRepository parentCategory;
    
    @Autowired
    private ImageRepository imageRepo;
    
    @Autowired
    private  ProductItemRepository productItemRepository;

    @Override
    public List<ProductDTO> getAllProducts() {
        List<product> products = repo.findAll();

        return products.stream().map(p -> {
            ProductDTO dto = new ProductDTO();
            dto.setProductId(p.getProductId());
            dto.setProductName(p.getName());
            dto.setDescription(p.getDescription());
            dto.setOriginalPrice(p.getOriginalPrice());
            dto.setSalePrice(p.getSalePrice());
            dto.setCategoryName(
                p.getCategory() != null ? p.getCategory().getName() : null
            );
            return dto;
        }).toList();
    }

    @Override
    public Optional<ProductDTO> getProductById(String pid) {
        return repo.findById(pid)
                   .map(p -> {
                       ProductDTO dto = new ProductDTO();
                       dto.setProductId(p.getProductId());
                       dto.setProductName(p.getName());
                       dto.setOriginalPrice(p.getOriginalPrice());
                       dto.setSalePrice(p.getSalePrice());
                       // add more fields if needed
                       return dto;
                   });
    }

    

	@Override
	public String saveproductDtls(ProductDTO pdto) {
		// Create entity from DTO
        product p = new product();
        p.setProductId(pdto.getProductId());
        p.setName(pdto.getProductName());
        p.setDescription(pdto.getDescription());
        p.setOriginalPrice(pdto.getOriginalPrice());
        p.setSalePrice(pdto.getSalePrice());

      //   Handle category if categoryName or ID is provided
        if (pdto.getProduct_category_id() != null) {
            ProductCategory category = categoryRepo.findById(pdto.getProduct_category_id())
                .orElseThrow(() -> new RuntimeException("Category not found: " + pdto.getProduct_category_id()));
            p.setCategory(category);
        }

        // Save to DB
        repo.save(p);

        return "Product saved with ID: " + p.getProductId();
	}

	@Override
	public String updateProductDtlsByProductId(String pid,ProductDTO pdto) {
		Optional<product> opt=	repo.findById(pid);
		if (opt.isEmpty()) {
	        throw new RuntimeException("Product with ID " + pid + " not found");
	    }		
		 product existing = opt.get();
		 
		 // Update fields if present in DTO
		    if (pdto.getProductName() != null) existing.setName(pdto.getProductName());
		    if (pdto.getDescription() != null) existing.setDescription(pdto.getDescription());
		    if (pdto.getOriginalPrice() != null) existing.setOriginalPrice(pdto.getOriginalPrice());
		    if (pdto.getSalePrice() != null) existing.setSalePrice(pdto.getSalePrice());
		    
		    //   Handle category if categoryName or ID is provided
	        if (pdto.getProduct_category_id() != null) {
	            ProductCategory category = categoryRepo.findById(pdto.getProduct_category_id())
	                .orElseThrow(() -> new RuntimeException("Category not found: " + pdto.getProduct_category_id()));
	            existing.setCategory(category);
	        }
	        repo.save(existing);

	        return "Product with ID " + pid + " updated successfully";
	}

	@Override
	@Transactional
	public String deleteProductDtlsByProductId(String pid) {
		
		Optional<product> op=repo.findById(pid);
		if(op.isEmpty()) {
		    throw new RuntimeException("Product with ID " + pid + " not found");
		}
		 product existing = op.get();
		
		 repo.deleteById(pid);

		    return "Product with ID " + pid + " deleted successfully";
	}

	
//	  @Override
//	    public List<ProductDTO> getProductsByCategoryId(String categoryId) {
//	        List<product> products = repo.findProductsByCategoryId(categoryId);
//
//	        return products.stream()
//	                .map(p -> new ProductDTO(
//	                        p.getProductId(),
//	                        p.getName(),
//	                        p.getDescription(),
//	                        p.getOriginalPrice(),
//	                        p.getSalePrice(),
//	                        (p.getCategory() != null) ? p.getCategory().getName() : null, // ✅ safe null handling
//	                                (p.getCategory() != null) ? p.getCategory().getId() : null   // ✅ safe null handling
//	                )).toList();
//	    }
    
//		  @Override
//		    public CategoryWithProductsDTO getGroupProductsByCategoryId(String categoryId) {
//		        // ✅ Get products for one category
//		        List<product> products = repo.findByCategory_Id(categoryId);
//	
//		        
//		        if (products.isEmpty()) {
//		            return null; // or throw exception if you prefer
//		        }
//	
//		        String categoryName = products.get(0).getCategory().getName();
//	
//		        List<ProductDTO> productDTOs = products.stream()
//		                .map(p -> new ProductDTO(
//		                        p.getProductId(),
//		                        p.getName(),
//		                        p.getDescription(),
//		                        p.getOriginalPrice(),
//		                        p.getSalePrice(),
//		                        p.getCategory().getName(),
//		                        p.getCategory().getId()
//		                ))
//		                .toList();
//	
//		        return CategoryWithProductsDTO.builder()
//		                .proCategoryId(categoryId)
//		                .cateoryName(categoryName)  // ✅ FIXED
////		                .cateoryImage(categoryImage) // optional if you want
////		                .productGenderId(genderId)   // optional if you want
////		                .parent_category_id(parentCategoryId) // optional if you want
//		                .products(productDTOs)
//		                .build();
//		    }
		  
		  
//		  @Override
//		  public List<CategoryWithProductsDTO> getAllCategoriesWithProducts() {
//		      // 🔍 Fetch all products
//		      List<product> products = repo.findAll();
//
//		      // 📦 Group by category
////		      Map<ProductCategory, List<product>> groupedByCategory = products.stream()
////		              .collect(Collectors.groupingBy(product::getCategory));
//		   // Create an empty Map to hold grouped data
//		    
//		      
//		      Map<ProductCategory, List<product>> groupedByCategory = new HashMap<>();
//		      // Loop through each product
//		      for (product p : products) {
//		          ProductCategory category = p.getCategory();
//
//		          // If the category is not yet in the map, add it with a new list
//		          if (!groupedByCategory.containsKey(category)) {
//		              groupedByCategory.put(category, new ArrayList<product>());
//		          }
//
//		          // Add the product to the correct category list
//		          groupedByCategory.get(category).add(p);
//		      }
//
//		      
////		      for (Map.Entry<ProductCategory, List<product>> entry : groupedByCategory.entrySet()) {
////		    	  ProductCategory category = entry.getKey();       // 👈 KEY
////		    	    List<product> productList = entry.getValue();    // 👈 VALUE
////
////		    	    System.out.println("Category Name: " + category.getName());
////		    	    System.out.println("Products: " + productList); 
////		      }
//
//		      // 🔄 Convert each group into CategoryWithProductsDTO
//		      return groupedByCategory.entrySet().stream()
//		              .map(entry -> {
//		                  ProductCategory category = entry.getKey();
//		                  List<ProductDTO> productDTOs = entry.getValue().stream()
//		                          .map(p -> new ProductDTO(
//		                                  p.getProductId(),
//		                                  p.getName(),
//		                                  p.getDescription(),
//		                                  p.getOriginalPrice(),
//		                                  p.getSalePrice(),
//		                                  p.getCategory().getName(),
//		                                  p.getCategory().getId()
//		                          ))
//		                          .toList();
//
//		                  return CategoryWithProductsDTO.builder()
//		                          .proCategoryId(category.getId())
//		                          .cateoryName(category.getName())
//		                          .cateoryImage(category.getImage())
//		                          .productGenderId(category.getGender() != null ? category.getGender().getId() : null)
//		                          .parent_category_id(category.getParentCategory() != null ? category.getParentCategory().getId() : null)
//		                          .products(productDTOs)
//		                          .build();
//		              })
//		              .toList();
//		  }
//		  
//		  @Override
//		  public List<CategoryWithProductsDTO> getAllCategoriesWithProducts() {
//
//		      // ✅ Fetch all products from DB
//		      List<product> products = repo.findAll();
//
//		      // ✅ Group manually (NO Java 8 streams)
//		      Map<ProductCategory, List<product>> groupedByCategory = new HashMap<>();
//
//		      for (product p : products) {
//		          ProductCategory category = p.getCategory();
//
//		          // If category not in map → add it
//		          if (!groupedByCategory.containsKey(category)) {
//		              groupedByCategory.put(category, new ArrayList<product>());
//		          }
//
//		          // Add product to the correct category list
//		          groupedByCategory.get(category).add(p);
//		      }
//
//		      // ✅ Prepare final DTO list
//		      List<CategoryWithProductsDTO> categoryList = new ArrayList<>();
//
//		      // ✅ Iterate through the map
//		      for (Map.Entry<ProductCategory, List<product>> entry : groupedByCategory.entrySet()) {
//		          ProductCategory category = entry.getKey();
//		          List<product> productList = entry.getValue();
//
//		          // ✅ Prepare ProductDTO list manually
//		          List<ProductDTO> productDTOs = new ArrayList<>();
//		          for (product p : productList) {
//		              ProductDTO dto = new ProductDTO(
//		                      p.getProductId(),
//		                      p.getName(),
//		                      p.getDescription(),
//		                      p.getOriginalPrice(),
//		                      p.getSalePrice(),
//		                      p.getCategory().getName(),
//		                      p.getCategory().getId()
//		              );
//		              productDTOs.add(dto);
//		          }
//
//		          // ✅ Build CategoryWithProductsDTO manually
//		          CategoryWithProductsDTO categoryDTO = CategoryWithProductsDTO.builder()
//		                  .proCategoryId(category.getId())
//		                  .cateoryName(category.getName())
//		                  .cateoryImage(category.getImage())
//		                  .productGenderId(category.getGender() != null ? category.getGender().getId() : null)
//		                  .parent_category_id(category.getParentCategory() != null ? category.getParentCategory().getId() : null)
//		                  .products(productDTOs)
//		                  .build();
//
//		          categoryList.add(categoryDTO);
//		      }
//
//		      // ✅ Return final list
//		      return categoryList;
//		  }

		@Override
		public List<genderDTO> getAllGenderByCategories() {
			// TODO Auto-generated method stub
			   List<ProductCategory> allCategories =categoryRepo.findAll();
			   
			   Map<ProductGender,List<ProductCategory>> goupByGender= new HashMap<>();
			   //convert entity to dto
			   for(ProductCategory g:allCategories) {
				  ProductGender pgender=g.getGender();
				  if(!goupByGender.containsKey(pgender)) {					
					  goupByGender.put(pgender, new ArrayList<ProductCategory>());
				  }
 			 goupByGender.get(pgender).add(g);
//				  goupByGender
//		            .computeIfAbsent(pgender, k -> new ArrayList<>())
//		            .add(g);   
			   }
			   
			   List<genderDTO> genderDTOList = new ArrayList<>(); 
			   for(Map.Entry<ProductGender,List<ProductCategory>> entry:goupByGender.entrySet()) {
				   ProductGender genderKey= entry.getKey();
				   List<ProductCategory> categoryValue=entry.getValue();				   
				   List<CategoryWithProductsDTO> pccatList=new  ArrayList<CategoryWithProductsDTO>();
				   for(ProductCategory cat:categoryValue) {
					   CategoryWithProductsDTO  dtos =  CategoryWithProductsDTO.builder()
							   .proCategoryId(cat.getId())
				                .cateoryName(cat.getName())
				                .cateoryImage(cat.getImage())
				                .productGenderId(cat.getGender() != null ? cat.getGender().getId() : "")
				                .parent_category_id(cat.getParentCategory() != null ? cat.getParentCategory().getId() : null)
				               // .products(null) // you can map products if needed here
				                .build();
					      pccatList.add(dtos);	
					    

				   }				   
				      genderDTO gDTO = genderDTO.builder()
				              .productGenderId(genderKey.getId())
				              .genderName(genderKey.getName())
				              .productDesc(genderKey.getDescription())
				              .genderAgainstCatList(pccatList)
				              .build();
				        genderDTOList.add(gDTO);
			   }
			   
			return genderDTOList;
		}

//		  @Override
//		  public List<genderDTO> getAllGenderByCategories() {
//		      List<ProductCategory> allCategories = categoryRepo.findAll();
//
//		      Map<ProductGender, List<ProductCategory>> groupByGender = new HashMap<>();
//
//		      for (ProductCategory g : allCategories) {
//		          ProductGender pgender = g.getGender();
//
//		          if (pgender == null) {
//		              System.out.println("⚠ Category " + g.getId() + " has NULL gender in Hibernate!");
//		              continue;  // ❌ Skip to avoid NullPointerException
//		          }
//
//		          // ✅ Add to map safely
//		          groupByGender.computeIfAbsent(pgender, k -> new ArrayList<>()).add(g);
//		      }
//
//		      List<genderDTO> genderDTOList = new ArrayList<>();
//
//		      for (Map.Entry<ProductGender, List<ProductCategory>> entry : groupByGender.entrySet()) {
//		          ProductGender genderKey = entry.getKey();
//		          List<ProductCategory> categoryValue = entry.getValue();
//
//		          // ✅ Convert ProductCategory list to DTO list
//		          List<CategoryWithProductsDTO> pccatList = new ArrayList<>();
//		          for (ProductCategory cat : categoryValue) {
//		              CategoryWithProductsDTO dto = CategoryWithProductsDTO.builder()
//		                      .proCategoryId(cat.getId())
//		                      .cateoryName(cat.getName())
//		                      .cateoryImage(cat.getImage())
//		                      .productGenderId(cat.getGender() != null ? cat.getGender().getId() : null)
//		                      .parent_category_id(cat.getParentCategory() != null ? cat.getParentCategory().getId() : null)
//		                      .build();
//		              pccatList.add(dto);
//		          }
//
//		          // ✅ Build genderDTO safely
//		          genderDTO gDTO = genderDTO.builder()
//		                  .productGenderId(genderKey.getId())
//		                  .genderName(genderKey.getName())
//		                  .productDesc(genderKey.getDescription())
//		                  .genderAgainstCatList(pccatList)
//		                  .build();
//
//		          genderDTOList.add(gDTO);
//		      }
//
//		      return genderDTOList;
//		  }
		
		@Override
		public List<genderDTO> getAllGender() {
		    // fetch all ProductGender from DB
		    List<ProductGender> genders =genderRepo.findByStatus(1);
		    // convert to DTO
		    List<genderDTO> dtoList = new ArrayList<>();
		    for (ProductGender gender : genders) {
		        genderDTO dto = genderDTO.builder()
		                .productGenderId(gender.getId())
		                .genderName(gender.getName())
		                .productDesc(gender.getDescription())
		                //.genderAgainstCatList(null)   // not needed here, can populate later if required
		                .build();
		        dtoList.add(dto);
		    }
		    
//		    return genderRepo.findAll()
//		            .stream()
//		            .map(g -> genderDTO.builder()
//		                    .productGenderId(g.getId())
//		                    .genderName(g.getName())
//		                    .productDesc(g.getDescription())
//		                    .genderAgainstCatList(null)
//		                    .build())
//		            .toList();

		    return dtoList;
		}
		public List<CategoryWithProductsDTO> getCategoryHierarchy() {
		    List<ProductCategory> allCategories = categoryRepo.findAll();

		    // Map of ID → DTO
		    Map<String, CategoryWithProductsDTO> dtoMap = new HashMap<>();
		    for (ProductCategory cat : allCategories) {
		        dtoMap.put(cat.getId(),
		            CategoryWithProductsDTO.builder()
		                .proCategoryId(cat.getId())
		                .cateoryName(cat.getName())
		                .cateoryImage(cat.getImage())
		                .productGenderId(cat.getGender() != null ? cat.getGender().getId() : null)
		                .parent_category_id(cat.getParentCategory() != null ? cat.getParentCategory().getId() : null)
		                .subCategories(new ArrayList<>())
		                .build()
		        );
		    }

		    // Build hierarchy
		    List<CategoryWithProductsDTO> rootCategories = new ArrayList<>();
		    for (ProductCategory cat : allCategories) {
		        CategoryWithProductsDTO currentDTO = dtoMap.get(cat.getId());

		        if (cat.getParentCategory() != null &&
		            !cat.getParentCategory().getId().equals(cat.getId())) {   // ✅ Avoid self reference
		            CategoryWithProductsDTO parentDTO = dtoMap.get(cat.getParentCategory().getId());
		            if (parentDTO != null) {
		                parentDTO.getSubCategories().add(currentDTO);
		            }
		        } else {
		            rootCategories.add(currentDTO);  // ✅ Top level (no parent OR parent=self)
		        }
		    }

		    return rootCategories;
		}

		public List<CategoryWithProductsDTO> getParentCategoryCombo() {
		    List<ParentCategory> allCategories = parentCategory.findAll();
		    List<CategoryWithProductsDTO> parentCategories = new ArrayList<>();
		    for (ParentCategory cat : allCategories) {
		            CategoryWithProductsDTO dto = CategoryWithProductsDTO.builder()
		                .parent_category_id(cat.getId())
		                .cateoryName(cat.getParentName())
		                .cateoryImage(cat.getParentImage())
		                .build();
		            parentCategories.add(dto);
		        }		    
		    return parentCategories;
		}

		  @Override
		  @Transactional(readOnly = true)
		  public List<CategoryWithProductsDTO> getProductCategoriesByParentId(String parentId,String deptId) {
			  //categoryRepo.findByParentCategory_Id(parentId)
		      return categoryRepo.findByParentCategory_IdAndGender_Id(parentId,deptId).stream()
		              .map(cat -> CategoryWithProductsDTO.builder()
		                      .proCategoryId(cat.getId())
		                      .cateoryName(cat.getName())
		                      .cateoryImage(cat.getImage())
		                      .parent_category_id(cat.getParentCategory() != null ? cat.getParentCategory().getId() : null)
		                      .build())
		              .toList();
		  }
		 


		  @Override
		  @Transactional(readOnly = true)
		  public List<ProductDTO> getProductsByCategoryIds(String categoryId) {
			  System.out.println("categoryId->"+repo.findByCategory_Id(categoryId));
		      return repo.findByCategory_Id(categoryId)
		              .stream()
		              .map(pro ->{
		                  List<ProductImage> images = imageRepo.findByProduct_ProductId(pro.getProductId());
		                  
//		                  String singleImage = (images != null && !images.isEmpty())
//		                          ? images.get(0).getFilename()
//		                          : null;
		                  String imageUrl = imageBaseUrl.endsWith("/") ? imageBaseUrl : imageBaseUrl + "/";
		                  List<ImageDTO> imageDTOs = (images != null)
		                          ? images.stream()
		                                  .map(img -> ImageDTO.builder()
		                                          .imageId(img.getId())
		                                          .productId(pro.getProductId())
		                                          .productItemId(img.getProductItem() != null ? img.getProductItem().getId() : null)
		                                          .imageFileName("assets/"+img.getFilename())
		                                          .build())
		                                  .toList()
		                          : List.of();

		             return ProductDTO.builder()
		                      .productId(pro.getProductId())
		                      .productName(pro.getName())
		                      .description(pro.getDescription())
		                      .originalPrice(pro.getOriginalPrice())
		                      .salePrice(pro.getSalePrice())
		                      .categoryName(pro.getCategory() != null ? pro.getCategory().getName() : null)
		                      .product_category_id(pro.getCategory() != null ? pro.getCategory().getId() : null)
		                      .images(imageDTOs)
		                      .build();
		                    	  })
		              .toList();
		  }

		@Override
		public List<ProductDTO> getProductsByCategoryId(String categoryId) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public CategoryWithProductsDTO getGroupProductsByCategoryId(String categoryId) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public List<CategoryWithProductsDTO> getAllCategoriesWithProducts() {
			// TODO Auto-generated method stub
			return null;
		}
		@Override
		@Transactional(readOnly = true)
		public Page<ProductDTO> getProductsByCategoryPaginationIds(
		        String categoryId,
		        Pageable pageable) {

		    Page<product> productPage =
		            repo.findByCategory_Id(categoryId, pageable);

		    return productPage.map(pro -> {

		        List<ImageDTO> imageDTOs =
		                pro.getImages() != null
		                ? pro.getImages().stream()
		                    .map(img -> ImageDTO.builder()
		                            .imageId(img.getId())
		                            .productId(pro.getProductId())
		                            .productItemId(img.getProductItem() != null
		                                    ? img.getProductItem().getId()
		                                    : null)
		                            .imageFileName("assets/" + img.getFilename())
		                            .build())
		                    .toList()
		                : List.of();

		        return ProductDTO.builder()
		                .productId(pro.getProductId())
		                .productName(pro.getName())
		                .description(pro.getDescription())
		                .originalPrice(pro.getOriginalPrice())
		                .salePrice(pro.getSalePrice())
		                .categoryName(pro.getCategory() != null
		                        ? pro.getCategory().getName()
		                        : null)
		                .product_category_id(pro.getCategory() != null
		                        ? pro.getCategory().getId()
		                        : null)
		                .images(imageDTOs)
		                .build();
		    });
		}
		
		
		@Override
		@Transactional(readOnly = true)
		public List<ProductDTO> getProductsByCategoryIdsSortBy(
		        String categoryId,
		        boolean ascOrder,
		        String... sortBy) {

		    // Create Sort object
		    Sort sort = Sort.by(
		            ascOrder ? Sort.Direction.ASC : Sort.Direction.DESC,
		            sortBy);

		    // Fetch products with sorting
		    List<product> products =
		            repo.findByCategory_Id(categoryId, sort);

		    return products.stream()
		            .map(pro -> {

		                List<ProductImage> images =
		                        imageRepo.findByProduct_ProductId(pro.getProductId());

		                List<ImageDTO> imageDTOs = (images != null)
		                        ? images.stream()
		                        .map(img -> ImageDTO.builder()
		                                .imageId(img.getId())
		                                .productId(pro.getProductId())
		                                .productItemId(img.getProductItem() != null ?
		                                        img.getProductItem().getId() : null)
		                                .imageFileName("assets/" + img.getFilename())
		                                .build())
		                        .toList()
		                        : List.of();

		                return ProductDTO.builder()
		                        .productId(pro.getProductId())
		                        .productName(pro.getName())
		                        .description(pro.getDescription())
		                        .originalPrice(pro.getOriginalPrice())
		                        .salePrice(pro.getSalePrice())
		                        .categoryName(pro.getCategory() != null ?
		                                pro.getCategory().getName() : null)
		                        .product_category_id(pro.getCategory() != null ?
		                                pro.getCategory().getId() : null)
		                        .images(imageDTOs)
		                        .build();
		            })
		            .toList();
		}

		@Override
	    @Transactional(readOnly = true)
	    public ProductItemResponse getProductItemById(String productItemId) {

	        // 1. Find Product Item
	        ProductItem productItem = productItemRepository
	                .findById(productItemId)
	                .orElseThrow();

	        // 2. Map Product Item and Product details into DTO
	        ProductItemResponse response = new ProductItemResponse();

	        response.setProductItemId(productItem.getId());

	        response.setProductId(
	                productItem.getProduct().getProductId());

	        response.setProductName(
	                productItem.getProduct().getName());

	        response.setOriginalPrice(
	                productItem.getOriginalPrice());

	        response.setSalePrice(
	                productItem.getSalePrice());

	        response.setProductCode(
	                productItem.getProductCode());

	        // 3. Return response
	        return response;
	    }

} 
