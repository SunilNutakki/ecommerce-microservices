package com.nt.model;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "product_image")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductImage {
    @Id
    @Column(name = "image_id", length = 7)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY) 
    @JoinColumn(name = "product_id", referencedColumnName = "product_id")
    @JsonBackReference 
    private product product;

    @ManyToOne(fetch = FetchType.LAZY) 
    @JoinColumn(name = "product_item_id", referencedColumnName = "pro_item_id")
    private ProductItem productItem;

    @Column(name = "image_filename", length = 400)
    private String filename;
}
