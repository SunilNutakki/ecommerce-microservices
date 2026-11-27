
package com.ecommerce.inventory.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ecommerce.inventory.entity.InventoryReservation;

import jakarta.persistence.LockModeType;

@Repository
public interface InventoryReservationRepository
        extends JpaRepository<InventoryReservation, String> {

    List<InventoryReservation> findByInventoryStock_StockId(String stockId);

    List<InventoryReservation> findByUserId(String userId);

    List<InventoryReservation> findByReservationStatus(String reservationStatus);

    Optional<InventoryReservation> findByCartItemIdAndReservationStatus(
            String cartItemId, String reservationStatus);

    List<InventoryReservation> findByOrderId(String orderId);
    
    @Query("""
    	    SELECT r
    	    FROM InventoryReservation r
    	    WHERE r.reservationStatus = 'RESERVED'
    	    AND r.expiresAt <= :currentTime
    	""")
    	List<InventoryReservation> findExpiredReservations(
    	        @Param("currentTime") LocalDateTime currentTime);
    
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
        SELECT r
        FROM InventoryReservation r
        WHERE r.reservationId = :reservationId
    """)
    Optional<InventoryReservation> findByIdForUpdate(
            @Param("reservationId") String reservationId);
    
    Optional<InventoryReservation> findByIdempotencyKey(
            String idempotencyKey
    );
}
