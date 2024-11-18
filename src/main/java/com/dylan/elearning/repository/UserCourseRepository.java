package com.dylan.elearning.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.dylan.elearning.entity.CompositeKey.UserCourseId;
import com.dylan.elearning.entity.Course;
import com.dylan.elearning.entity.User;
import com.dylan.elearning.entity.UserCourse;

import java.util.Optional;

public interface UserCourseRepository extends JpaRepository<UserCourse, UserCourseId> {

    @Query("SELECT uc FROM UserCourse uc " +
            "WHERE uc.user = :user " +
            "AND uc.course = :course")
    Optional<UserCourse> getUserCourse(
            @Param("user") User user,
            @Param("course") Course course
    );

}

