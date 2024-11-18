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
public class ChapterUpdateReq {

    @Size(max = 50, message = "Chapter name must be less than 255 characters.")
    private String name;

    @Size(max = 1000, message = "Description must be less than 1000 characters.")
    private String description;

    private Integer status;

    @Min(value = 1, message = "Order must be greater than 0.")
    private Integer order;
}
