package com.dylan.elearning.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.dylan.elearning.entity.Chapter;
import com.dylan.elearning.entity.Course;
import com.dylan.elearning.exception.AppException;
import com.dylan.elearning.repository.ChapterRepository;
import com.dylan.elearning.repository.CourseRepository;

@Component
public class ValidateUtils {

    @Autowired
    private ChapterRepository chapterRepository;

    @Autowired
    private CourseRepository courseRepository;

    public Chapter validateCourseAndChapter(Long courseId, Long chapterId) {
        Course course = courseRepository.getCourseById(courseId)
                .orElseThrow(() -> new AppException("Course not found with id: " + courseId));

        Chapter chapter = chapterRepository.getChapterById(chapterId)
                .orElseThrow(() -> new AppException("Chapter not found with id: " + chapterId));


        if (!chapter.getCourse().getId().equals(course.getId())) {
            throw new AppException("Chapter does not belong to the specified course");
        }

        return chapter;
    }

}
