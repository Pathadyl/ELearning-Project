package com.dylan.elearning.dto.request.admin;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;
import com.dylan.elearning.dto.SortOption;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@SuperBuilder
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ChapterSearchReq {

    private String name;

    private String status;

    @Min(value = 1, message = "Course ID must be a positive number.")
    private Long courseId;

    private String createdDateFrom; // yyyy-MM-dd HH:mi:ss

    private String createdDateTo;   // yyyy-MM-dd HH:mi:ss

    private Integer pageIndex;      // >= 0

    private Integer pageSize;       // <= 1

    private List<SortOption> sort;
}
