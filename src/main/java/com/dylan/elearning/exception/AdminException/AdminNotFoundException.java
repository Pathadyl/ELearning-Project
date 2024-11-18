package com.dylan.elearning.exception.AdminException;

import com.dylan.elearning.exception.AppException;

public class AdminNotFoundException extends AppException {
    public AdminNotFoundException(String message) {
        super(message);
    }
}
