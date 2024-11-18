package com.dylan.elearning.dto.request.user;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;
import com.dylan.elearning.dto.SortOption;


import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@SuperBuilder
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)

public class UserSearchCourseReq {

    @NotNull(message = "Forbidden action!")
    @Min(value = 1, message = "User ID must be a positive number.")
    private Long userId;

    private String name;

    private String status;

    private String teacherName;

    private LocalDate createdDateFrom;

    private LocalDate createdDateTo;

    private Double ratingFrom;

    private Double ratingTo;

    private Integer numLessons;

    private Integer pageIndex;

    private Integer pageSize;

    private List<SortOption> sort;

}
