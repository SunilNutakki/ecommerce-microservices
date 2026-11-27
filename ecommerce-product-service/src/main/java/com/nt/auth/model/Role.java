package com.nt.auth.model;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

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
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
@Entity
@Table(name="oprauth_roles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class Role {
	
	@Id
	@Column(name="role_id",length=10)
	private String id;
	
	@Column(name="role_name",length=50,unique = true,nullable = false)
	private String roleName;
	
	@Column(name="role_description",length=255)
	private String roleDesc;
	
	@Column(name="status",length=1)
	private String status;
	
	 @Column(name = "created_by", length = 50)
	    private String createdBy;

	    @Column(name = "created_date")
	    private java.sql.Timestamp createdDate;
	    
	    
	 // One Role → Many Users
	    @OneToMany(mappedBy = "role", fetch = FetchType.LAZY)
	    @JsonManagedReference
	    @ToString.Exclude
	    private List<User> users;

	    // One Role → Many Context Mappings
	    @OneToMany(mappedBy = "role", fetch = FetchType.LAZY)
	    @JsonManagedReference
	    @ToString.Exclude
	    private List<Context> contextList;
	
}
