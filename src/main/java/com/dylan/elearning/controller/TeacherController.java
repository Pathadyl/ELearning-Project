package com.dylan.elearning.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.dylan.elearning.constant.UrlConstant;
import com.dylan.elearning.dto.request.admin.TeacherCreateReq;
import com.dylan.elearning.dto.request.admin.TeacherSearchReq;
import com.dylan.elearning.dto.request.admin.TeacherUpdateReq;
import com.dylan.elearning.dto.response.admin.TeacherResDto;
import com.dylan.elearning.dto.response.admin.TeacherSearchRes;
import com.dylan.elearning.entity.Teacher;
import com.dylan.elearning.service.ITeacherService;

import java.util.Objects;

@RestController
@RequestMapping(UrlConstant.API_V1)
@RequiredArgsConstructor
public class TeacherController {

    private final ITeacherService teacherService;

    /**
     * Create new a teacher.
     *
     * @param req đối tượng {@link TeacherCreateReq} chứa thông tin teacher cần tạo.
     * @return một TeacherResDto chứa đối tượng {@link Teacher} đã được tạo.
     * @see ITeacherService#createTeacher(TeacherCreateReq)
     */
    @PostMapping(UrlConstant.ADD_TEACHERS)
    public TeacherResDto createTeacher(@Valid @RequestBody TeacherCreateReq req) {
        return teacherService.createTeacher(req);
    }


    /**
     * update thông tin một teacher cụ thể.
     *
     * @param teacherId ID của Teacher cần cập nhật.
     * @param req       đối tượng {@link TeacherUpdateReq} chứa thông tin update cho teacher.
     * @return          TeacherResDto chứa đối tượng {@link Teacher} đã được cập nhật.
     * @see ITeacherService#updateTeacher(Long, TeacherUpdateReq)
     */
    @PutMapping(UrlConstant.UPDATE_TEACHERS)
    public TeacherResDto updateTeacher(@PathVariable("teacher_id") Long teacherId,
                                       @Valid @RequestBody TeacherUpdateReq req) {
        return teacherService.updateTeacher(teacherId, req);
    }

    /**
     * delete một teacher cụ thể.
     * @param teacherId ID của teacher cần xóa.
     * @return          ResponseEntity với thông báo thành công.
     * @see ITeacherService#deleteTeacher(Long)
     */
    @DeleteMapping(UrlConstant.DELETE_TEACHERS)
    public ResponseEntity<?> deleteTeacher(@PathVariable("teacher_id") Long teacherId) {
        teacherService.deleteTeacher(teacherId);
        return ResponseEntity.ok("Teacher deleted successfully.");
    }

    /**
     * Lấy danh sách Teacher theo phân trang dựa trên các tiêu chí tìm kiếm.
     * <p>
     * Các tiêu chí tìm kiếm bao gồm các field như tên người dùng, tên teacher, trạng thái và khoảng thời gian.
     * </p>
     *
     * @param req         đối tượng {@link TeacherSearchReq} chứa các bộ lọc tìm kiếm.
     * @return            TeacherSearchRes chứa {@link TeacherSearchRes} với kết quả tìm kiếm.
     * @see ITeacherService#getTeachers(TeacherSearchReq)
     */
    @GetMapping(UrlConstant.GET_TEACHERS)
    public TeacherSearchRes getTeachers(@RequestBody(required = false) TeacherSearchReq req) {
        return teacherService.getTeachers(req);
    }
}

