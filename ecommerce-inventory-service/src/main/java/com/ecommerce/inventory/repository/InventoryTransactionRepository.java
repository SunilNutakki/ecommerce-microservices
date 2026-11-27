
package com.ecommerce.inventory.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ecommerce.inventory.entity.InventoryTransaction;

@Repository
public interface InventoryTransactionRepository
        extends JpaRepository<InventoryTransaction, String> {

    List<InventoryTransaction> findByInventoryStock_StockIdOrderByCreatedAtDesc(
            String stockId);

    List<InventoryTransaction> findByTransactionType(String transactionType);

    List<InventoryTransaction> findByReferenceId(String referenceId);

    List<InventoryTransaction> findByReferenceType(String referenceType);
    
    @Query("""
    	    SELECT t
    	    FROM InventoryTransaction t
    	    JOIN FETCH t.inventoryStock
    	    ORDER BY t.createdAt DESC
    	""")
    	List<InventoryTransaction> findAllWithStock();
    @Query("""
    	    SELECT t
    	    FROM InventoryTransaction t
    	    JOIN FETCH t.inventoryStock
    	    WHERE t.inventoryStock.stockId = :stockId
    	    ORDER BY t.createdAt DESC
    	""")
    	List<InventoryTransaction> findTransactionsWithStock(
    	        @Param("stockId") String stockId);
}
