
package com.ecommerce.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.ecommerce.inventory.dto.InventoryStockResponse;
import com.ecommerce.inventory.dto.InventoryTransactionResponse;
import com.ecommerce.inventory.dto.InventoryReservationResponse;
import com.ecommerce.inventory.entity.InventoryStock;
import com.ecommerce.inventory.entity.InventoryTransaction;
import com.ecommerce.inventory.entity.InventoryReservation;

@Mapper(componentModel = "spring")
public interface InventoryMapper {

    InventoryStockResponse toStockResponse(InventoryStock inventoryStock);

    @Mapping(
        source = "inventoryStock.stockId",
        target = "stockId"
    )
    InventoryReservationResponse toReservationResponse(
            InventoryReservation reservation);
    
    @Mapping(source = "inventoryStock.stockId", target = "stockId")
    InventoryTransactionResponse toTransactionResponse(
            InventoryTransaction transaction);
}
