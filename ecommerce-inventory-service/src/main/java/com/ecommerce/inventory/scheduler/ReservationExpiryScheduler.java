
package com.ecommerce.inventory.scheduler;

import java.time.LocalDateTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.ecommerce.inventory.entity.InventoryReservation;
import com.ecommerce.inventory.repository.InventoryReservationRepository;
import com.ecommerce.inventory.service.ReservationExpiryService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
@ConditionalOnProperty(
    name = "inventory.scheduler.enabled",
    havingValue = "true",
    matchIfMissing = true
)
public class ReservationExpiryScheduler {

    private static final Logger log =
            LoggerFactory.getLogger(ReservationExpiryScheduler.class);

    private final InventoryReservationRepository reservationRepository;
    private final ReservationExpiryService expiryService;

    @Scheduled(fixedDelay = 30000)
    public void processExpiredReservations() {

        List<InventoryReservation> expiredReservations =
                reservationRepository.findExpiredReservations(
                        LocalDateTime.now());

        for (InventoryReservation reservation : expiredReservations) {

            try {
                boolean expired = expiryService.expireReservation(
                        reservation.getReservationId());

                if (expired) {
                    log.info("Reservation expired successfully: {}",
                            reservation.getReservationId());
                }

            } catch (Exception ex) {
                log.error("Failed to expire reservation: {}",
                        reservation.getReservationId(), ex);
            }
        }
    }
}
