package com.nt.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Entity
@Table(name = "product_variation")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductVariation {
    @Id
    @Column(name = "variation_id", length = 7)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_item_id", referencedColumnName = "pro_item_id")
    private ProductItem productItem;

    @ManyToOne(fetch = FetchType.LAZY)		
    @JoinColumn(name = "size_id")
    private ProductSize size;

    @Column(name = "qty_in_stock")
    private Integer qtyInStock;
}
