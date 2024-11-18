package com.dylan.elearning.exception.LessonException;

import com.dylan.elearning.exception.AppException;

public class LessonNotFoundException extends AppException {
    public LessonNotFoundException(String message) {
        super(message);
    }
}
