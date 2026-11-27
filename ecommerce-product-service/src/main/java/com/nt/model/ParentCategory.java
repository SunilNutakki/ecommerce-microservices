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
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Entity
@Table(name = "parent_category")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ParentCategory {
		@Id
		@GeneratedValue(strategy = GenerationType.UUID)
		@Column(name = "parent_id", length = 36)
	    private String id;

	    @Column(name = "parent_name", length = 100)
	    private String parentName;

	    @Column(name = "parent_image")
	    private String parentImage;
	    
	    @OneToMany(mappedBy = "parentCategory",cascade = CascadeType.ALL,fetch=FetchType.LAZY)
	    @JsonManagedReference  
	     private List<ProductCategory>  productCategoryList;  
	    
}
