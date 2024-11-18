package com.dylan.elearning.service;

import com.dylan.elearning.dto.request.admin.AdminLoginReq;
import com.dylan.elearning.dto.response.admin.*;

public interface IAdminService {
    AdminResponseDto login(AdminLoginReq req);
    UserResponseDto getUserInfo(Long userId);
    UserCourseInfoRes getUserCourseInfo(Long userId, Long courseId);
    UserCourseLessonInfoRes getUserCourseLessonInfo(Long userId, Long courseId, Long lessonId);
    TeacherInfoRes getTeacherInfo(Long teacherId);
    CourseInfoRes getCourseInfo(Long courseId);
    ChapterInfoRes getChapterInfo(Long chapterId);
    LessonInfoRes getLessonInfo(Long lessonInfo);

}
