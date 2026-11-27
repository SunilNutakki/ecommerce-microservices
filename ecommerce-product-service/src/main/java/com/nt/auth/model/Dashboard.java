package com.nt.auth.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

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
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "oprdash_dashboard_setup")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class Dashboard {
	@Id
	@Column(name="dashboard_id",length=10)
	private String dashBoardId;
	
	@Column(name="dashboard_name",length=100,nullable = false)
	private String dashBoardName;
	
	@Column(name="url_path",length= 150,nullable = false)
	private String urlPath;
	
	@Column(name="description",length=255)
	private String description;
	
	@Column(name = "is_active", length = 1)
    private String isActive;
	
	// One dashboard → Many Context mappings
	@OneToMany(mappedBy="dashboard", fetch = FetchType.LAZY)
	@JsonManagedReference
	@ToString.Exclude
	 private List<Context> contextList;
}
