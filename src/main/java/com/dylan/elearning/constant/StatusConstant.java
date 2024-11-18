package com.dylan.elearning.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum StatusConstant {

    START("START"),
    PROCESSING("PROCESSING"),
    DONE("DONE")
    ;

    private final String value;
}
