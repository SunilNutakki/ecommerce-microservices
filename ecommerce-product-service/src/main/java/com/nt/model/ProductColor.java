package com.nt.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "product_color")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductColor {
    @Id
    @Column(name = "color_id", length = 7)
    private String id;

    @Column(name = "color_name", length = 100)
    private String name;
}
