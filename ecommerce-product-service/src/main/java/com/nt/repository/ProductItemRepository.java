package com.nt.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nt.model.ProductItem;

@Repository
public interface ProductItemRepository
        extends JpaRepository<ProductItem, String> {

    @EntityGraph(attributePaths = {"product"})
    Optional<ProductItem> findById(String id);

}