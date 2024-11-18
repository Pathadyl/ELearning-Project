package com.dylan.elearning.entity.CompositeKey;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class UserCourseLessonId implements Serializable {
    private Long userId;
    private Long courseId;
    private Long lessonId;
}