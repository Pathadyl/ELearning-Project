package com.dylan.elearning.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.dylan.elearning.constant.UrlConstant;
import com.dylan.elearning.dto.request.admin.AdminLoginReq;
import com.dylan.elearning.service.IAdminService;

@RestController
@RequestMapping(UrlConstant.API_V1 + "/admins")
public class AdminController {

    // Using constructor injection
    @Autowired
    @Qualifier("adminServiceImpl")
    private IAdminService adminService;

    /**
     * Đăng nhập cho Admin
     *
     * @param req Thông tin đăng nhập của Admin
     * @return Thông tin đăng nhập Admin
     */
    @PostMapping(UrlConstant.LOGIN)
    public ResponseEntity<?> login(@Valid @RequestBody AdminLoginReq req) {
        return ResponseEntity.ok(adminService.login(req));
    }


    /**
     * Hiển thị thông tin của user, bao gồm số lượng khóa học đã đăng ký
     *
     * @param userId ID của người dùng
     * @return Thông tin của người dùng và số lượng khóa học đã đăng ký
     */
    @GetMapping(UrlConstant.USER_INFO)
    public ResponseEntity<?> getUserInfo(@PathVariable("user_id") Long userId) {

        return ResponseEntity.ok(adminService.getUserInfo(userId));
    }


    /**
     * Hiển thị chi tiết thông tin user_course, bao gồm thông tin user, thông tin khóa học,
     * số lượng bài học đang xử lý và đã hoàn thành trong khóa học
     *
     * @param userId ID của userId
     * @param courseId ID của course
     * @return Thông tin chi tiết của user_course
     */
    @GetMapping(UrlConstant.USER_COURSE_INFO)
    public ResponseEntity<?> getUserCourseInfo(
            @PathVariable("user_id") Long userId,
            @PathVariable("course_id") Long courseId) {

        return ResponseEntity.ok(adminService.getUserCourseInfo(userId, courseId));
    }


    /**
     * Hiển thị chi tiết bài học và trạng thái của người dùng, bao gồm thông tin user,
     * khóa học và bài học
     *
     * @param userId ID của user
     * @param courseId ID của course
     * @param lessonId ID của lesson
     * @return Thông tin chi tiết của bài học và trạng thái học của người dùng
     */
    @GetMapping(UrlConstant.USER_COURSE_LESSON_INFO)
    public ResponseEntity<?> getUserCourseLessonInfo(
            @PathVariable("user_id") Long userId,
            @PathVariable("course_id") Long courseId,
            @PathVariable("lesson_id") Long lessonId) {

        return ResponseEntity.ok(adminService.getUserCourseLessonInfo(userId, courseId, lessonId));
    }


    /**
     * Hiển thị thông tin chi tiết về giáo viên và số lượng khóa học giáo viên đã dạy
     *
     * @param teacherId ID của giáo viên
     * @return Thông tin chi tiết của giáo viên và số lượng khóa học đã dạy
     */
    @GetMapping(UrlConstant.TEACHER_INFO)
    public ResponseEntity<?> getTeacherInfo(@PathVariable("teacher_id") Long teacherId) {

        return ResponseEntity.ok(adminService.getTeacherInfo(teacherId));
    }


    /**
     * Hiển thị chi tiết khóa học, bao gồm số lượng chương, bài học và số lượng người dùng đã đăng ký khóa học
     *
     * @param courseId ID của khóa học
     * @return Thông tin chi tiết của khóa học
     */
    @GetMapping(UrlConstant.COURSE_INFO)
    public ResponseEntity<?> getCourseInfo(@PathVariable("course_id") Long courseId) {

        return ResponseEntity.ok(adminService.getCourseInfo(courseId));
    }


    /**
     * Hiển thị thông tin chi tiết về chương, bao gồm số lượng bài học trong chương và chi tiết chương học
     *
     * @param chapterId ID của chương học
     * @return Thông tin chi tiết của chương học
     */
    @GetMapping(UrlConstant.CHAPTER_INFO)
    public ResponseEntity<?> getChapterInfo(@PathVariable("chapter_id") Long chapterId) {

        return ResponseEntity.ok(adminService.getChapterInfo(chapterId));
    }


    /**
     * Hiển thị thông tin chi tiết về bài học, bao gồm thông tin chương học, số lượng người dùng đã hoàn thành bài học,
     * và số lượng người đang học bài học
     *
     * @param lessonId ID của bài học
     * @return Thông tin chi tiết của bài học
     */
    @GetMapping(UrlConstant.LESSON_INFO)
    public ResponseEntity<?> getLessonInfo(@PathVariable("lesson_id") Long lessonId) {

        return ResponseEntity.ok(adminService.getLessonInfo(lessonId));
    }
}
