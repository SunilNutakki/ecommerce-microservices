package com.nt.auth.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.nt.auth.model.Context;
import com.nt.auth.projection.User_Dash_BeanView;

@Repository
public interface IContextRepository extends JpaRepository<Context, String> {
	
	 @Query("""
		        SELECT 
		            r.roleName AS roleName,
		            d.dashBoardName AS dashboardName,
		            d.urlPath AS urlPath
		        FROM Context c
		        JOIN c.role r
		        JOIN c.dashboard d
		        WHERE r.id = :roleId
		    """)
		    List<User_Dash_BeanView> findDashboardsByRole(@Param("roleId") String roleId);
   
}


