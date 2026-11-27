package com.nt.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Entity
@Table(name = "product")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class product {
    @Id
    @Column(name = "product_id", length = 7)
    private String productId;

    @Column(name = "product_name", length = 255)
    private String name;

    @Column(name = "product_desc", length = 100)
    private String description;

    @Column(name = "original_price")
    private Integer originalPrice;

    @Column(name = "sale_price")
    private Integer salePrice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_category_id")
  //  @JoinColumn(name = "product_category_id", nullable = true)
    private ProductCategory category;
    
    @OneToMany(mappedBy = "product", orphanRemoval = true, cascade = CascadeType.ALL,fetch=FetchType.LAZY)
    @JsonManagedReference 
    private List<ProductImage> images;
    
    
}
