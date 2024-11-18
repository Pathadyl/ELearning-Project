package com.dylan.elearning.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TypeConstant {

    VIDEO("VIDEO"),
    PDF("PDF"),
    DOCX("DOCX"),
    SUCE("SUCE")
    ;

    private final String value;
}