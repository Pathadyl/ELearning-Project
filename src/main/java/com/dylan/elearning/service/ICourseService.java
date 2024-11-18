package com.dylan.elearning.service;

import com.dylan.elearning.dto.request.admin.CourseCreateReq;
import com.dylan.elearning.dto.request.admin.CourseSearchReq;
import com.dylan.elearning.dto.request.admin.CourseUpdateReq;
import com.dylan.elearning.dto.response.admin.CourseResponseDto;
import com.dylan.elearning.dto.response.admin.CourseSearchRes;

public interface ICourseService {
    CourseResponseDto createCourse(CourseCreateReq req);
    CourseResponseDto updateCourse(Long courseId, CourseUpdateReq req);
    void softDeleteCourse(Long courseId);
    CourseSearchRes getCourses(CourseSearchReq req);
}
