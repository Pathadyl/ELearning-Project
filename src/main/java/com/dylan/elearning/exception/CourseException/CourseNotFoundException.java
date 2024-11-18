package com.dylan.elearning.exception.CourseException;

import com.dylan.elearning.exception.AppException;

public class CourseNotFoundException extends AppException {
    public CourseNotFoundException(String message) {
        super(message);
    }
}
