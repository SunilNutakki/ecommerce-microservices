package com.nt.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "product_size")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductSize {
    @Id
    @Column(name = "size_id", length = 7)
    private String id;

    @Column(name = "size_name", length = 100)
    private String name;

    @Column(name = "size_order")
    private Integer order;
}

