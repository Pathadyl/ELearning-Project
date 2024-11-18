package com.dylan.elearning.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ConfigConstant {

    ACTIVE("ACTIVE", 1),
    INACTIVE("INACTIVE", 0);

    private final String value;
    private final int code;

}
