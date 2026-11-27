package com.nt.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.nt.model.product;


@Repository
public interface ICustomProductRepo extends JpaRepository<product, String> {
	
	@Query("SELECT p FROM product p WHERE p.category.id = :catId")
	List<product> findProductsByCategoryId(@Param("catId") String categoryId);
	
	  //List<product> findByCategoryId(String categoryId);
	  @EntityGraph(attributePaths = {"images", "category"})
	  
	  List<product> findByCategory_Id(String categoryId);
	 
	  @EntityGraph(attributePaths = {"images", "category"})
	  Page<product>findByCategory_Id(String categoryId,Pageable pageable);
	  
	  @EntityGraph(attributePaths = {"images", "category"})
	  List<product> findByCategory_Id(String categoryId,Sort sort);
	
}