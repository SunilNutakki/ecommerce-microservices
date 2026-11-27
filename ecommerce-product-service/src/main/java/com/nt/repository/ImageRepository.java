package com.nt.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nt.model.ProductImage;
@Repository
public interface ImageRepository  extends JpaRepository<ProductImage, String> {

	public List<ProductImage> findByProduct_ProductId(String productId);
}
