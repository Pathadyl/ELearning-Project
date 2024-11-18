package com.dylan.elearning.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import com.dylan.elearning.entity.CompositeKey.UserCourseLessonId;
import com.dylan.elearning.entity.Lesson;
import com.dylan.elearning.entity.UserCourse;
import com.dylan.elearning.entity.UserCourseLesson;

import java.util.List;
import java.util.Optional;

public interface UserCourseLessonRepository extends JpaRepository<UserCourseLesson, UserCourseLessonId> {

    @Query("SELECT ucl " +
            "FROM UserCourseLesson ucl " +
            "WHERE ucl.userCourse = :uc " +
            "AND ucl.lesson = :lesson")
    Optional<UserCourseLesson> getUserCourseLesson(UserCourse uc, Lesson lesson);

    @Query("SELECT ucl " +
            "FROM UserCourseLesson ucl " +
            "WHERE ucl.userCourse = :uc")
    List<UserCourseLesson> getListUserCourseLesson(UserCourse uc);

}
