package com.dylan.elearning.dto.request.admin;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

@NoArgsConstructor
@AllArgsConstructor
@Data
@SuperBuilder
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class LessonUpdateReq {

    @Size(max = 50, message = "Name must not exceed 50 characters.")
    private String name;

    @Size(max = 1000, message = "Description must not exceed 1000 characters.")
    private String description;

    private Integer status;   // 0 or 1

    @Size(max = 50, message = "Type must not exceed 50 characters.")
    private String type;

    @Size(max = 255, message = "URL must not exceed 255 characters.")
    private String url;

    @Min(value = 1, message = "Order must be greater than or equal to 1.")
    private Integer order;

    private Long chapterId;
}
