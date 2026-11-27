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
@Table(name="oprauth_auth_providers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Provider {
	@Id
	@Column(name="provider_id",length = 25)
	private String Id;
	
	@Column(name="provider_name",length=10)
	private String providerName;
	
	@Column(name="status",length=2)
	private String status;
	 
	
	@OneToMany(mappedBy = "provider", fetch = FetchType.LAZY)
	@JsonManagedReference
	@ToString.Exclude
    private List<User> users;
}
