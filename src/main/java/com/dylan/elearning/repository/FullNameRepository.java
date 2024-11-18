package com.dylan.elearning.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.dylan.elearning.entity.FullName;

public interface FullNameRepository extends JpaRepository <FullName, Long> {


}
