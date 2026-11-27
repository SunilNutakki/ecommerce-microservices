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
@Table(name="oprauth_country")
@Getter
@Setter
@NoArgsConstructor
public class country {
	@Id
	@Column(name="oprauth_country_code_id",length=15)
	private String countryCode;
	@Column(name="oprauth_country_code",length=15)
	private String countryId;
	@Column(name="oprauth_country_name",length=15)
	private String countryName;
	
	 // One countrycode → Many Users
    @OneToMany(mappedBy = "country", fetch = FetchType.LAZY)
    @JsonManagedReference
    @ToString.Exclude
    private List<User> users;
}
