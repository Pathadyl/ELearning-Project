package com.dylan.elearning.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Data
public class AppException extends RuntimeException {
    private String message;
    private Date date;
    private String field;

    public AppException(String message) {
        super(message);
        this.message = message;
        date = new Date();
    }

}

