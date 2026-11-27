
package com.ecommerce.inventory.service;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ecommerce.inventory.entity.InventoryReservation;
import com.ecommerce.inventory.entity.InventoryStock;
import com.ecommerce.inventory.entity.InventoryTransaction;
import com.ecommerce.inventory.repository.InventoryReservationRepository;
import com.ecommerce.inventory.repository.InventoryStockRepository;
import com.ecommerce.inventory.repository.InventoryTransactionRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReservationExpiryService {

    private final InventoryReservationRepository reservationRepository;
    private final InventoryStockRepository stockRepository;
    private final InventoryTransactionRepository transactionRepository;

    @Transactional
    public boolean expireReservation(String reservationId) {

        InventoryReservation reservation = reservationRepository
                .findByIdForUpdate(reservationId)
                .orElse(null);

        if (reservation == null) {
            return false;
        }

        // Recheck status and expiry while holding the reservation lock.
        if (!"RESERVED".equals(reservation.getReservationStatus())
                || reservation.getExpiresAt() == null
                || reservation.getExpiresAt().isAfter(LocalDateTime.now())) {
            return false;
        }

        InventoryStock stock = stockRepository
                .findByProductItemIdForUpdate(reservation.getProductItemId())
                .orElseThrow(() -> new IllegalStateException(
                        "Stock not found for product item: "
                                + reservation.getProductItemId()));

        int quantity = reservation.getQuantity();

        if (stock.getReservedQuantity() < quantity) {
            throw new IllegalStateException(
                    "Insufficient reserved quantity for reservation: "
                            + reservationId);
        }

        // Restore stock.
        stock.setReservedQuantity(stock.getReservedQuantity() - quantity);
        stock.setAvailableQuantity(stock.getAvailableQuantity() + quantity);

        stockRepository.save(stock);

        // Update reservation status.
        reservation.setReservationStatus("EXPIRED");
        reservationRepository.save(reservation);

        // Record stock movement.
        InventoryTransaction transaction = new InventoryTransaction();

        transaction.setTransactionId(generateTransactionId());
        transaction.setInventoryStock(stock);
        transaction.setTransactionType("EXPIRED");
        transaction.setQuantity(quantity);
        transaction.setReferenceId(reservation.getReservationId());
        transaction.setReferenceType("RESERVATION");
        transaction.setRemarks("Stock reservation expired automatically");
        transaction.setCreatedBy(reservation.getUserId());

        transactionRepository.save(transaction);

        return true;
    }

    private String generateTransactionId() {
        return "ITX" + UUID.randomUUID()
                .toString()
                .replace("-", "")
                .substring(0, 16)
                .toUpperCase();
    }
}
