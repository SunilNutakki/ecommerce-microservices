package com.ecommerce.inventory.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import jakarta.persistence.EntityManager;
import com.ecommerce.inventory.client.ProductServiceClient;
import com.ecommerce.inventory.dto.ProductItemResponse;
import com.ecommerce.inventory.dto.ReserveStockRequest;
import com.ecommerce.inventory.entity.InventoryReservation;
import com.ecommerce.inventory.entity.InventoryStock;
import com.ecommerce.inventory.entity.InventoryTransaction;
import com.ecommerce.inventory.repository.InventoryReservationRepository;
import com.ecommerce.inventory.repository.InventoryStockRepository;
import com.ecommerce.inventory.repository.InventoryTransactionRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InventoryReservationTransactionService {

    private final InventoryStockRepository stockRepository;
    private final InventoryReservationRepository reservationRepository;
    private final InventoryTransactionRepository transactionRepository;
    private final ProductServiceClient productServiceClient;
    private final EntityManager entityManager;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public InventoryReservation createReservation(
            ReserveStockRequest request,
            String idempotencyKey) {

        ProductItemResponse product =
                productServiceClient.getProductItemById(
                        request.getProductItemId());

        if (product == null) {
            throw new IllegalArgumentException(
                    "Product item not found: "
                            + request.getProductItemId());
        }

        InventoryStock stock = stockRepository
                .findByProductItemIdForUpdate(
                        request.getProductItemId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Inventory stock not found for product item: "
                                + request.getProductItemId()));

        if (stock.getAvailableQuantity() < request.getQuantity()) {
            throw new IllegalArgumentException(
                    "Insufficient stock. Available quantity: "
                            + stock.getAvailableQuantity());
        }

        stock.setAvailableQuantity(
                stock.getAvailableQuantity() - request.getQuantity());

        stock.setReservedQuantity(
                stock.getReservedQuantity() + request.getQuantity());

        stockRepository.save(stock);

        InventoryReservation reservation =
                new InventoryReservation();

        reservation.setReservationId(generateId("RES"));
        reservation.setInventoryStock(stock);
        reservation.setProductItemId(stock.getProductItemId());
        reservation.setVariationId(stock.getVariationId());
        reservation.setCartId(request.getCartId());
        reservation.setCartItemId(request.getCartItemId());
        reservation.setUserId(request.getUserId());
        reservation.setQuantity(request.getQuantity());
        reservation.setIdempotencyKey(idempotencyKey);
        reservation.setReservationStatus("RESERVED");
        reservation.setExpiresAt(
                LocalDateTime.now().plusMinutes(15));

        reservation = reservationRepository.saveAndFlush(reservation);

     // Reload generated timestamp from database
     entityManager.refresh(reservation);

        InventoryTransaction transaction =
                new InventoryTransaction();

        transaction.setTransactionId(generateId("ITX"));
        transaction.setInventoryStock(stock);
        transaction.setTransactionType("RESERVED");
        transaction.setQuantity(request.getQuantity());
        transaction.setReferenceId(reservation.getReservationId());
        transaction.setReferenceType("RESERVATION");
        transaction.setRemarks("Stock reserved for cart item");
        transaction.setCreatedBy(request.getUserId());

        transactionRepository.save(transaction);

        return reservation;
    }

    private String generateId(String prefix) {
        return prefix + java.util.UUID.randomUUID()
                .toString()
                .replace("-", "")
                .substring(0, 16)
                .toUpperCase();
    }
}