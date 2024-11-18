package com.dylan.elearning.exception.CourseException;

import com.dylan.elearning.exception.AppException;

public class CourseInactiveException extends AppException {
    public CourseInactiveException(String message) {
        super(message);
    }
}
