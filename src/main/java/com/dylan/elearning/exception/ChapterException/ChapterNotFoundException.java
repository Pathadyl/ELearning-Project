package com.dylan.elearning.exception.ChapterException;

import com.dylan.elearning.exception.AppException;

public class ChapterNotFoundException extends AppException {
    public ChapterNotFoundException(String message) {
        super(message);
    }
}
