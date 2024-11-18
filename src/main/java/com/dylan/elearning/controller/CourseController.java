package com.dylan.elearning.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import com.dylan.elearning.constant.UrlConstant;
import com.dylan.elearning.dto.request.admin.CourseCreateReq;
import com.dylan.elearning.dto.request.admin.CourseSearchReq;
import com.dylan.elearning.dto.request.admin.CourseUpdateReq;
import com.dylan.elearning.dto.response.admin.CourseResponseDto;
import com.dylan.elearning.dto.response.admin.CourseSearchRes;
import com.dylan.elearning.entity.Course;
import com.dylan.elearning.service.ICourseService;

import java.util.Objects;

@RestController
@RequestMapping(UrlConstant.API_V1)
@RequiredArgsConstructor
public class CourseController {

    private final ICourseService courseService;

    /**
     * Tạo mới một khóa học.
     *
     * @param req đối tượng {@link CourseCreateReq} chứa thông tin course cần tạo.
     * @return một CourseResponseDto chứa đối tượng {@link Course} đã được tạo.
     * @see ICourseService#createCourse(CourseCreateReq)
     */
    @PostMapping(UrlConstant.ADD_COURSES)
    public CourseResponseDto addCourse(@Valid @RequestBody CourseCreateReq req) {
        CourseResponseDto createdCourse = courseService.createCourse(req);
        return createdCourse;
    }

    /**
     * Update thông tin một khóa học cụ thể.
     *
     * @param courseId ID của khóa học cần cập nhật.
     * @param req      đối tượng {@link CourseUpdateReq} chứa thông tin update cho khóa học.
     * @return         CourseResponseDto chứa đối tượng {@link Course} đã được cập nhật.
     * @see ICourseService#updateCourse(Long, CourseUpdateReq)
     */
    @PutMapping(UrlConstant.UPDATE_COURSES)
    public CourseResponseDto updateCourse(@PathVariable("course_id") Long courseId,
                                          @Valid @RequestBody CourseUpdateReq req) {
        CourseResponseDto updatedCourse = courseService.updateCourse(courseId, req);
        return updatedCourse;
    }

    /**
     * Xóa một khóa học cụ thể (soft delete)
     *
     * @param courseId ID của course cần xóa.
     * @return         ResponseEntity với thông báo thành công.
     * @see ICourseService#softDeleteCourse(Long)
     */
    @DeleteMapping(UrlConstant.DELETE_COURSES)
    public void softDeleteCourse(@PathVariable("course_id") Long courseId) {
        courseService.softDeleteCourse(courseId);
    }

    /**
     * Lấy danh sách khóa học theo phân trang dựa trên các tiêu chí tìm kiếm.
     * <p>
     * Các tiêu chí tìm kiếm bao gồm các field như tên khóa học, trạng thái, tên giáo viên và khoảng thời gian.
     * </p>
     *
     * @param courseReq         đối tượng {@link CourseSearchReq} chứa các bộ lọc tìm kiếm.
     * @return            ResponseEntity chứa {@link CourseSearchRes} với kết quả tìm kiếm.
     * @see ICourseService#getCourses(CourseSearchReq)
     */
    @GetMapping(UrlConstant.GET_COURSES)
    public CourseSearchRes getCourses(@RequestBody(required = false) CourseSearchReq courseReq) {
        return courseService.getCourses(courseReq);
    }
}

