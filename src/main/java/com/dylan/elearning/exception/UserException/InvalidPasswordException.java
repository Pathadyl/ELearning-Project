package com.dylan.elearning.exception.UserException;

import com.dylan.elearning.exception.AppException;

public class InvalidPasswordException extends AppException {
    public InvalidPasswordException(String message) {
        super(message);
    }

}

