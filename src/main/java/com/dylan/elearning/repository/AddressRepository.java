package com.dylan.elearning.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.dylan.elearning.entity.Address;

public interface AddressRepository extends JpaRepository <Address, Long> {
}
