package com.dylan.elearning.exception.UserException;

import com.dylan.elearning.exception.AppException;

public class UserNotFoundException extends AppException {
    public UserNotFoundException(String message) {
        super(message);
    }
}
