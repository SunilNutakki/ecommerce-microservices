package com.nt.auth.model;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;

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
@Table(name = "oprauth_users")
@Data
@NoArgsConstructor
@AllArgsConstructor


public class User {
	@Id
	@Column(name="user_id",length=15)
	private String userId;
	
	@Column(name="user_full_name",length = 100, nullable = false)
	private String userFullName;
	
	@Column(name="email",length = 150, unique = true)
	private String email;
	
	@Column(name="phone", length = 15, unique = true)
	private String phoneNo;
	
	@Column(name = "password", length = 255, nullable = false)
	private String password;
	
	@ManyToOne(targetEntity=Role.class,cascade=CascadeType.ALL,fetch = FetchType.LAZY)
	@JoinColumn(name = "role_id" ,referencedColumnName = "role_id" )
	@JsonBackReference
    private Role role;
	
	@ManyToOne(targetEntity = Provider.class,cascade = CascadeType.ALL,fetch=FetchType.LAZY)
	@JoinColumn(name="provider_id",referencedColumnName ="provider_id" )
	@JsonBackReference
	private Provider provider;
	
	
	@ManyToOne(targetEntity =UserStatus.class ,cascade = CascadeType.ALL,fetch=FetchType.LAZY)
	@JoinColumn(name="status_id")
	@JsonBackReference
	private UserStatus userStatus;
	
	@ManyToOne(targetEntity = country.class,cascade = CascadeType.ALL,fetch=FetchType.LAZY)
	@JoinColumn(name="country_code_id")
	@JsonBackReference
	private country country;
	
	 /** AUDIT FIELDS **/
    @Column(name = "created_at")
    private java.sql.Timestamp createdAt;

    @Column(name = "updated_at")
    private java.sql.Timestamp updatedAt;

}
