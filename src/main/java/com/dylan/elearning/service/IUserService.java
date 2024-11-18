package com.dylan.elearning.service;

import com.dylan.elearning.dto.request.user.*;
import com.dylan.elearning.dto.response.admin.UserSearchRes;
import com.dylan.elearning.dto.response.user.*;
import com.dylan.elearning.dto.response.user.DetailResponse.CourseResponseDto;
import com.dylan.elearning.dto.response.user.DetailResponse.UserResponseDto;

public interface IUserService {
    UserResponseDto register(UserRegisterReq req);
    UserResponseDto login(UserLoginReq req);
    UserResponseDto updateUser(Long userId, UserUpdateReq req);
    void deleteUser(Long userId);
    UserSearchRes getUsers(UserSearchReq req);
    UserEnrollCourseRes enrollCourse(Long userId, Long courseId);
    UserRateCourseRes rateCourse(Long userId, Long courseId, UserRateCourseReq req);
    UserReviewCourseRes reviewCourse(Long userId, Long courseId, UserReviewCourseReq req);
    CourseResponseDto getCourseInfo(Long courseId);
    UserSearchCourseRes getRegisterCourse(UserSearchCourseReq req);
    UserStudyRes study(Long userId, Long courseId, Long lessonId, UserStudyReq req);
}
