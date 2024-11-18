package com.dylan.elearning.exception.UserException;

import com.dylan.elearning.exception.AppException;

public class UserInactiveException extends AppException {
    public UserInactiveException(String message) {
        super(message);
    }
}
