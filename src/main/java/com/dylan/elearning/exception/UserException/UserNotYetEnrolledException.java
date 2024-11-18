package com.dylan.elearning.exception.UserException;

import com.dylan.elearning.exception.AppException;

public class UserNotYetEnrolledException extends AppException {
    public UserNotYetEnrolledException(String message) {
        super(message);
    }
}
