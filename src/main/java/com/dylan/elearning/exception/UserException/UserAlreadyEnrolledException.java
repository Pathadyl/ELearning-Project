package com.dylan.elearning.exception.UserException;

import com.dylan.elearning.exception.AppException;

public class UserAlreadyEnrolledException extends AppException {
    public UserAlreadyEnrolledException(String message) {
        super(message);
    }
}
