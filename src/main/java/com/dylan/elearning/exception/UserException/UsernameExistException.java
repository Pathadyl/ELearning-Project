package com.dylan.elearning.exception.UserException;

import com.dylan.elearning.exception.AppException;

public class UsernameExistException extends AppException {
    public UsernameExistException(String message) {
        super(message);
    }
}
