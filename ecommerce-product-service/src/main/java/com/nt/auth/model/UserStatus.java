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
@Table(name="oprauth_user_status")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserStatus {
	@Id
	@Column(name="status_id",length = 25)
	private String Id;
	
	@Column(name="status_name",length=25)
	private String statusName;

	@Column(name="active_status",length=5)
	private String activeStatus;
	
	@Column(name="created_by",length=15)
	private String createdBY;
	
	 // One satats → Many Users
    @OneToMany(targetEntity =User.class,cascade = CascadeType.ALL,mappedBy = "userStatus", fetch = FetchType.LAZY)
    @JsonManagedReference
    @ToString.Exclude
    private List<User> users;
    
	
	
	
}
