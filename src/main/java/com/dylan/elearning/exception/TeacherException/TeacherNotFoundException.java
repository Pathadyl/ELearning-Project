package com.dylan.elearning.exception.TeacherException;

import com.dylan.elearning.exception.AppException;

public class TeacherNotFoundException extends AppException {
    public TeacherNotFoundException(String message) {
        super(message);
    }
}
