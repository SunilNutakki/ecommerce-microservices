package com.nt.repository;


import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nt.dto.ProductDTO;
import com.nt.model.ProductCategory;


@Repository
public interface ProductCategoryRepository extends JpaRepository<ProductCategory, String> {
   // List<ProductCategory> findByParentCategory_Id(String parentId);
    List<ProductCategory> findByParentCategory_IdAndGender_Id(String parentId, String deptId);
    

}
