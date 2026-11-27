package com.ecommerce.cartservice.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "cart_status")
@Data
public class CartStatus {
    @Id
    @Column(name = "status_id", length = 10)
    private String statusId;

    @Column(name = "status_name", nullable = false, unique = true, length = 30)
    private String statusName;

    @Column(name = "description")
    private String description;

    @Column(name = "is_active", nullable = false)
    private Boolean active = true;

}
