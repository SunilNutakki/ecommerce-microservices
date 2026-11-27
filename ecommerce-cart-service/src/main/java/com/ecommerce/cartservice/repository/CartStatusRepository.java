package com.ecommerce.cartservice.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ecommerce.cartservice.entity.CartStatus;
@Repository
public interface CartStatusRepository extends JpaRepository<CartStatus, String> {
    Optional<CartStatus> findByStatusName(String statusName);
}
