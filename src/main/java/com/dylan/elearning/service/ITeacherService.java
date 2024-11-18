package com.dylan.elearning.service;

import com.dylan.elearning.dto.request.admin.TeacherCreateReq;
import com.dylan.elearning.dto.request.admin.TeacherSearchReq;
import com.dylan.elearning.dto.request.admin.TeacherUpdateReq;
import com.dylan.elearning.dto.response.admin.TeacherResDto;
import com.dylan.elearning.dto.response.admin.TeacherSearchRes;

public interface ITeacherService {
    TeacherResDto createTeacher(TeacherCreateReq req);
    TeacherResDto updateTeacher(Long teacherId, TeacherUpdateReq req);
    void deleteTeacher(Long teacherId);
    TeacherSearchRes getTeachers(TeacherSearchReq req);
}
