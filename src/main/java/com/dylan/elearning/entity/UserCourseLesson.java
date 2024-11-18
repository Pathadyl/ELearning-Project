package com.dylan.elearning.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import com.dylan.elearning.entity.CompositeKey.UserCourseLessonId;

import java.util.Date;

@Entity
@Table(name = "user_course_lesson")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserCourseLesson {

    @EmbeddedId
    private UserCourseLessonId id;

    @ManyToOne
    @MapsId("userId")
    private User user;

    @ManyToOne
    @MapsId("courseId")
    private Course course;

    @ManyToOne
    @MapsId("lessonId")
    private Lesson lesson;

    @ManyToOne
    @MapsId("UserCourseId")
    @JoinColumns({
            @JoinColumn(name = "user_id", referencedColumnName = "user_id"),
            @JoinColumn(name = "course_id", referencedColumnName = "course_id")
    })
    private UserCourse userCourse;

    @Column(name = "status")
    private String status;

    @Column(name = "created_date")
    @CreatedDate
    private Date createdDate;

    @Column(name = "updated_date")
    @LastModifiedDate
    private Date updatedDate;

}