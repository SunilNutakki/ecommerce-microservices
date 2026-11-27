package com.nt.repository;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nt.dto.ProductDTO;
import com.nt.model.ParentCategory;


@Repository
public interface ParentCategoryRepository  extends JpaRepository<ParentCategory, String>  {
	

}


