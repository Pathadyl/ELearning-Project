package com.dylan.elearning.exception.LessonException;

import com.dylan.elearning.exception.AppException;

public class NoLessonInCourseException extends AppException {
    public NoLessonInCourseException(String message) {
        super(message);
    }

}

