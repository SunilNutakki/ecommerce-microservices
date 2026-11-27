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
@Table(name="oprdash_context_setup")
@Data
@NoArgsConstructor
public class Context {
	@Id
	@Column(name="context_id",length=9)
	private String contextId;
	
//	@Column(name="role_id",length=25)
//	private String roleId;
//	
//	@Column(name="dashboard_id",length=25)
//	private String dashboardId; 
	
	 // --------- ROLE FK ----------
    @ManyToOne(targetEntity = Role.class,cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id",referencedColumnName = "role_id", nullable = false)   // FK column
    @JsonBackReference
    private Role role;   // map to oprauth_roles

    // --------- DASHBOARD FK ----------
    @ManyToOne(targetEntity = Dashboard.class,cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @JoinColumn(name = "dashboard_id",referencedColumnName ="dashboard_id" , nullable = false)  // FK column
    @JsonBackReference
    private Dashboard dashboard;  // map to oprdash_dashboard_setup
}
