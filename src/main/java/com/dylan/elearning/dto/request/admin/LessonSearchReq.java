package com.dylan.elearning.dto.request.admin;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
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
public class LessonSearchReq {

    private String name;

    private String status;

    private Long courseId;

    private Long chapterId;

    private String type;

    private String createdDateFrom; // yyyy-MM-dd HH:mi:ss

    private String createdDateTo;   // yyyy-MM-dd HH:mi:ss

    private Integer pageIndex;      // >= 0

    private Integer pageSize;       // <= 1

    private List<SortOption> sort;
}
