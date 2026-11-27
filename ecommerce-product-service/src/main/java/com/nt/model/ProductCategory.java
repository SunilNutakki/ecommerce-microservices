package com.nt.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "product_category")
//@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductCategory {

	
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "pro_category_id", length = 10)
    private String id;

    @Column(name = "category_name", length = 100)
    private String name;

    @Column(name = "category_image", length = 400)
    private String image;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pro_dept_id", referencedColumnName = "pro_dept_id")
    private ProductGender gender;

    // Refers to a separate parent_category table
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_category_id", referencedColumnName = "parent_id")
    @JsonBackReference 
    private ParentCategory parentCategory;
    
    
    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<product> products;
    
}


