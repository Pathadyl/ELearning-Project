package com.dylan.elearning.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Page;
import com.dylan.elearning.entity.Course;
import com.dylan.elearning.entity.User;

import java.time.LocalDate;
import java.util.Optional;


public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT u " +
            "FROM User u " +
            "WHERE u.username = :username")
    Optional<User> findUserByUsername(@Param("username") String username);

    @Query("SELECT u " +
            "FROM User u " +
            "WHERE u.username = :username " +
            "AND u.status = 'active'")
    Optional<User> findActiveUserByUsername(@Param("username") String username);

    @Query("SELECT u " +
            "FROM User u " +
            "WHERE u.id = :id")
    Optional<User> getUserById(@Param("id") Long id);

    @Query("SELECT u FROM User u " +
            "WHERE (:name IS NULL OR u.fullName.firstName LIKE CONCAT('%', :name, '%') " +
            "                     OR u.fullName.midName LIKE CONCAT('%', :name, '%') " +
            "                     OR u.fullName.lastName LIKE CONCAT('%', :name, '%')) " +
//            "AND (:age IS NULL OR s.age = :age) " +
            "AND (:username IS NULL OR u.username = :username) " +
            "AND (:status IS NULL OR u.status = :status) " +
            "AND (:createdDateFrom IS NULL OR u.createdDate >= :createdDateFrom) " +
            "AND (:createdDateTo IS NULL OR u.createdDate <= :createdDateTo)")
    Page<User> findUsers(
            @Param("name") String name,
//            @Param("age") Integer age,
            @Param("username") String username,
            @Param("status") String status,
            @Param("createdDateFrom") LocalDate createdDateFrom,
            @Param("createdDateTo") LocalDate createdDateTo,
            Pageable pageable
    );

    @Query("SELECT c " +
            "FROM Course c " +
            "INNER JOIN UserCourse uc ON c.id = uc.userCourseId.courseId " +
            "WHERE uc.userCourseId.userId = :userId " +
            "AND (:name IS NULL OR c.name LIKE CONCAT('%', :name, '%')) " +
            "AND (:status IS NULL OR c.status = :status) " +
            "AND (:teacherName IS NULL OR c.teacher.name = :teacherName) " +
            "AND (:createdDateFrom IS NULL OR c.createdDate >= :createdDateFrom) " +
            "AND (:createdDateTo IS NULL OR c.createdDate <= :createdDateTo)" +
            "AND (:ratingFrom IS NULL OR (" +
            "                             SELECT AVG(uc.rating) " +
            "                             FROM UserCourse sc " +
            "                             WHERE uc.userCourseId.courseId = c.id" +
            "                            ) >= :ratingFrom) " +
            "AND (:ratingTo IS NULL OR (" +
            "                           SELECT AVG(uc.rating) " +
            "                           FROM UserCourse sc " +
            "                           WHERE uc.userCourseId.courseId = c.id" +
            "                           ) <= :ratingTo)" +
            "AND (:numLessons IS NULL OR (" +
            "                             SELECT COUNT(l) " +
            "                             FROM Lesson l " +
            "                             WHERE l.chapter.id IN (" +
            "                                                    SELECT ch.id " +
            "                                                    FROM Chapter ch " +
            "                                                    WHERE ch.course.id = c.id" +
            "                                                   )" +
            "                             ) = :numLessons)")
    Page<Course> findRegisterCourses(
            @Param("userId") Long userId,
            @Param("name") String name,
            @Param("status") String status,
            @Param("teacherName") String teacherName,
            @Param("createdDateFrom") LocalDate createdDateFrom,
            @Param("createdDateTo") LocalDate createdDateTo,
            @Param("ratingFrom") Double ratingFrom,
            @Param("ratingTo") Double ratingTo,
            @Param("numLessons") Integer numLessons,
            Pageable pageable
    );

}
