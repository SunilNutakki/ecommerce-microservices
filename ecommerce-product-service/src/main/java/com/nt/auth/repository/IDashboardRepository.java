package com.nt.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nt.auth.model.Dashboard;
@Repository
public interface IDashboardRepository extends JpaRepository<Dashboard, String> {
	

}