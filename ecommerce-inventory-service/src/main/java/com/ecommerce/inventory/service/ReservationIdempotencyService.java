package com.ecommerce.inventory.service;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.ecommerce.inventory.entity.InventoryReservation;
import com.ecommerce.inventory.repository.InventoryReservationRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReservationIdempotencyService {

    private final InventoryReservationRepository reservationRepository;

    @Transactional(
        propagation = Propagation.REQUIRES_NEW,
        readOnly = true
    )
    public Optional<InventoryReservation> findExistingReservation(
            String idempotencyKey) {

        return reservationRepository.findByIdempotencyKey(idempotencyKey);
    }
}