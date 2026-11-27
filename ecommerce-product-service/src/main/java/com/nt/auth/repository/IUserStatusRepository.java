package com.nt.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nt.auth.model.UserStatus;
@Repository
public interface IUserStatusRepository extends JpaRepository<UserStatus, String> {

}
