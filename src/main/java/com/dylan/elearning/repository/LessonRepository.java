package com.dylan.elearning.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.dylan.elearning.entity.Lesson;

import java.util.Optional;
import java.util.Date;

public interface LessonRepository extends JpaRepository<Lesson, Long> {

    @Query("SELECT l " +
            "FROM Lesson l " +
            "WHERE l.id = :id")
    Optional<Lesson> getLessonById(@Param("id") Long id);


    @Query("SELECT l FROM Lesson l " +
            "LEFT JOIN l.chapter c " +
            "LEFT JOIN c.course cr " +
            "WHERE (:name IS NULL OR l.name LIKE CONCAT('%', :name, '%')) " +
            "AND (:status IS NULL OR l.status = :status) " +
            "AND (:courseId IS NULL OR cr.id = :courseId) " +
            "AND (:chapterId IS NULL OR c.id = :chapterId) " +
            "AND (:type IS NULL OR l.type = :type) " +
            "AND (:createdDateFrom IS NULL OR l.createdDate >= :createdDateFrom) " +
            "AND (:createdDateTo IS NULL OR l.createdDate <= :createdDateTo)")
    Page<Lesson> findLessons(@Param("name") String name,
                             @Param("status") String status,
                             @Param("courseId") Long courseId,
                             @Param("chapterId") Long chapterId,
                             @Param("type") String type,
                             @Param("createdDateFrom") Date createdDateFrom,
                             @Param("createdDateTo") Date createdDateTo,
                             Pageable pageable);

}

