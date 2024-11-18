package com.dylan.elearning.exception.UserException;

import com.dylan.elearning.exception.AppException;

public class UserCourseNotFoundException extends AppException {
    public UserCourseNotFoundException(String message) {
        super(message);
    }
}
