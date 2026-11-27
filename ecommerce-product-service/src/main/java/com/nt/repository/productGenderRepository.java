package com.nt.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nt.model.ProductCategory;
import com.nt.model.ProductGender;

@Repository
public interface productGenderRepository extends JpaRepository<ProductGender, String>{
	
	List<ProductGender> findByStatus(int status);


}
