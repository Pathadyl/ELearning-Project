package com.dylan.elearning.dto.response.user.DetailResponse;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@SuperBuilder
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)

public class CourseResponseDto {

    private String name;

    private String description;

    private String status;

    private TeacherResponseDto teacher;

    private List<ChapterResponseDto> chapters;

    private Integer numChapter;

    private Integer numLesson;

    private String createdDate;

    private String updatedDate;

    public CourseResponseDto(String name, String description, String status, List<ChapterResponseDto> chapters, TeacherResponseDto teacher) {
        this.name = name;
        this.description = description;
        this.status = status;
        this.chapters = chapters;
        this.teacher = teacher;
        this.numChapter = chapters.size();
        this.numLesson = chapters.stream()
                .mapToInt(ch -> ch.lessons.size())
                .sum();
    }
}
