
package com.ecommerce.inventory.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ecommerce.inventory.entity.InventoryStock;

import jakarta.persistence.LockModeType;

@Repository
public interface InventoryStockRepository
        extends JpaRepository<InventoryStock, String> {

    Optional<InventoryStock> findByProductItemId(String productItemId);

    boolean existsByProductItemId(String productItemId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT s FROM InventoryStock s WHERE s.productItemId = :productItemId")
    Optional<InventoryStock> findByProductItemIdForUpdate(
            @Param("productItemId") String productItemId);
}
