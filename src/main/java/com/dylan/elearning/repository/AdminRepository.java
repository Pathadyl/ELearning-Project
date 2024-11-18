package com.dylan.elearning.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.dylan.elearning.entity.Admin;

import java.util.Optional;

public interface AdminRepository extends JpaRepository<Admin, Long> {

    @Query("SELECT a " +
            "FROM Admin a " +
            "WHERE a.username = :username " +
            "AND a.status = 'active' ")
    Optional<Admin> findActiveAdminByUsername(@Param("username") String username);

}
