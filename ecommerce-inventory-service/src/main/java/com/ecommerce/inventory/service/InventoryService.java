
package com.ecommerce.inventory.service;

import java.util.List;

import com.ecommerce.inventory.dto.AddStockRequest;
import com.ecommerce.inventory.dto.InventoryReservationResponse;
import com.ecommerce.inventory.dto.InventoryStockResponse;
import com.ecommerce.inventory.dto.InventoryTransactionResponse;
import com.ecommerce.inventory.dto.ReserveStockRequest;
import com.ecommerce.inventory.entity.InventoryTransaction;

public interface InventoryService {

    List<InventoryStockResponse> getAllStock();

    InventoryStockResponse getStockByProductItemId(String productItemId);

    InventoryStockResponse addStock(AddStockRequest request);

    InventoryReservationResponse reserveStock(ReserveStockRequest request);

    InventoryReservationResponse releaseStock(String reservationId);
    InventoryReservationResponse confirmReservation(String reservationId);
    InventoryReservationResponse cancelReservation(String reservationId);
    
    InventoryReservationResponse getReservationById(String reservationId);
    
    List<InventoryTransactionResponse> getAllTransactions();

    List<InventoryTransactionResponse> getTransactionsByStockId(String stockId);
}
