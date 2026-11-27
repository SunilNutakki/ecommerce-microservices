package com.nt.model;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "product_item")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductItem {
    @Id
    @Column(name = "pro_item_id", length = 7)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY) 
    @JoinColumn(name = "product_id", referencedColumnName = "product_id")
    private product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "color_id", referencedColumnName = "color_id")
    private ProductColor color;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "size_id")
    private ProductSize size;

    @Column(name = "original_price")
    private Integer originalPrice;

    @Column(name = "sale_price")
    private Integer salePrice;

    @Column(name = "product_code", length = 100)
    private String productCode;
    
    @OneToMany(mappedBy = "productItem", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<ProductImage> images;
}
