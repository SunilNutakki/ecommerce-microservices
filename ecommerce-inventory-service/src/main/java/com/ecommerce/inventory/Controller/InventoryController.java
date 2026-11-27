
package com.ecommerce.inventory.Controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ecommerce.inventory.dto.AddStockRequest;
import com.ecommerce.inventory.dto.InventoryReservationResponse;
import com.ecommerce.inventory.dto.InventoryStockResponse;
import com.ecommerce.inventory.dto.InventoryTransactionResponse;
import com.ecommerce.inventory.dto.ReserveStockRequest;
import com.ecommerce.inventory.service.InventoryService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/api/inventory")
@Tag(
    name = "Inventory Management",
    description = "APIs for managing stock, reservations and inventory transactions"
)
public class InventoryController {

    private final InventoryService inventoryService;

    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    // API 1: Get all stock
    @Operation(
        summary = "Get all inventory stock",
        description = "Retrieves stock balances for all product items."
    )
    @ApiResponse(responseCode = "200", description = "Stock records retrieved successfully")
    @GetMapping
    public ResponseEntity<List<InventoryStockResponse>> getAllStock() {
        return ResponseEntity.ok(inventoryService.getAllStock());
    }

    // API 2: Get stock by product item ID
    @Operation(
        summary = "Get stock by product item ID",
        description = "Retrieves the stock balance for a specific product item."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Stock retrieved successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid request")
    })
    @GetMapping("/product/{productItemId}")
    public ResponseEntity<InventoryStockResponse> getStockByProductItemId(
        @Parameter(description = "Product item ID", example = "PI001")
        @PathVariable String productItemId) {

        return ResponseEntity.ok(
            inventoryService.getStockByProductItemId(productItemId));
    }

    // API 3: Add incoming stock
    @Operation(
        summary = "Add incoming stock",
        description = "Adds incoming quantity to the available inventory stock."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Stock added successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid quantity or product item")
    })
    @PostMapping("/{productItemId}/add")
    public ResponseEntity<InventoryStockResponse> addStock(
        @Parameter(description = "Product item ID", example = "PI001")
        @PathVariable String productItemId,
        @RequestBody AddStockRequest request) {

        request.setProductItemId(productItemId);

        return ResponseEntity.ok(inventoryService.addStock(request));
    }

    // API 4: Reserve stock
    @Operation(
        summary = "Reserve inventory stock",
        description = "Reserves available stock for a cart item. Supports idempotency keys to prevent duplicate reservations."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Stock reserved successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid request or insufficient stock")
    })
    @PostMapping("/reserve")
    public ResponseEntity<InventoryReservationResponse> reserveStock(
        @RequestBody ReserveStockRequest request) {

        InventoryReservationResponse response =
            inventoryService.reserveStock(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // API 5: Release reservation
    @Operation(
        summary = "Release stock reservation",
        description = "Releases a reservation and returns its quantity to available stock."
    )
    @ApiResponse(responseCode = "200", description = "Reservation released successfully")
    @PutMapping("/reservation/{reservationId}/release")
    public ResponseEntity<InventoryReservationResponse> releaseStock(
        @Parameter(description = "Reservation ID", example = "RES123456789")
        @PathVariable String reservationId) {

        return ResponseEntity.ok(inventoryService.releaseStock(reservationId));
    }

    // API 6: Confirm reservation
    @Operation(
        summary = "Confirm stock reservation",
        description = "Confirms a previously reserved quantity."
    )
    @ApiResponse(responseCode = "200", description = "Reservation confirmed successfully")
    @PutMapping("/reservation/{reservationId}/confirm")
    public ResponseEntity<InventoryReservationResponse> confirmReservation(
        @Parameter(description = "Reservation ID", example = "RES123456789")
        @PathVariable String reservationId) {

        return ResponseEntity.ok(inventoryService.confirmReservation(reservationId));
    }

    // API 7: Cancel reservation
    @Operation(
        summary = "Cancel stock reservation",
        description = "Cancels a reservation according to its current lifecycle status."
    )
    @ApiResponse(responseCode = "200", description = "Reservation cancelled successfully")
    @PutMapping("/reservation/{reservationId}/cancel")
    public ResponseEntity<InventoryReservationResponse> cancelReservation(
        @Parameter(description = "Reservation ID", example = "RES123456789")
        @PathVariable String reservationId) {

        return ResponseEntity.ok(inventoryService.cancelReservation(reservationId));
    }

    // API 8: Get reservation by ID
    @Operation(
        summary = "Get reservation by ID",
        description = "Retrieves reservation details using its reservation ID."
    )
    @ApiResponse(responseCode = "200", description = "Reservation retrieved successfully")
    @GetMapping("/reservation/{reservationId}")
    public ResponseEntity<InventoryReservationResponse> getReservationById(
        @Parameter(description = "Reservation ID", example = "RES123456789")
        @PathVariable String reservationId) {

        return ResponseEntity.ok(inventoryService.getReservationById(reservationId));
    }

    // API 9: Get all inventory transactions
    @Operation(
        summary = "Get all inventory transactions",
        description = "Retrieves the inventory stock movement history."
    )
    @ApiResponse(responseCode = "200", description = "Transactions retrieved successfully")
    @GetMapping("/transactions")
    public ResponseEntity<List<InventoryTransactionResponse>> getAllTransactions() {
        return ResponseEntity.ok(inventoryService.getAllTransactions());
    }

    // API 10: Get transactions by stock ID
    @Operation(
        summary = "Get transactions by stock ID",
        description = "Retrieves stock movement history for a specific inventory stock record."
    )
    @ApiResponse(responseCode = "200", description = "Transactions retrieved successfully")
    @GetMapping("/transactions/{stockId}")
    public ResponseEntity<List<InventoryTransactionResponse>> getTransactionsByStockId(
        @Parameter(description = "Inventory stock ID", example = "STK00001")
        @PathVariable String stockId) {

        return ResponseEntity.ok(
            inventoryService.getTransactionsByStockId(stockId));
    }
}
