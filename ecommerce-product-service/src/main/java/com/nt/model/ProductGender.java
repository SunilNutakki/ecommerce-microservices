package com.nt.model;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "product_department")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductGender {
    @Id
    @Column(name = "pro_dept_id", length = 25)
    private String id;

    @Column(name = "pro_dept_name", length = 50)
    private String name;

    @Column(name = "product_desc", length = 2000)
    private String description;
    
    @Column(name = "status", length = 10)
    private int status;

    
//    @OneToMany(mappedBy = "gender", cascade = CascadeType.ALL)
//    private List<ProductCategory> productCategory;
    
    @OneToMany(mappedBy = "gender", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude  
    private List<ProductCategory> productCategory;
}

