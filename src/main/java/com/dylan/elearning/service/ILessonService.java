package com.dylan.elearning.service;

import com.dylan.elearning.dto.request.admin.LessonCreateReq;
import com.dylan.elearning.dto.request.admin.LessonSearchReq;
import com.dylan.elearning.dto.request.admin.LessonUpdateReq;
import com.dylan.elearning.dto.response.admin.LessonResDto;
import com.dylan.elearning.dto.response.admin.LessonSearchRes;

import java.util.List;

public interface ILessonService {
    List<LessonResDto> addLessons(Long courseId, Long chapterId, List<LessonCreateReq> req);
    LessonResDto updateLesson(Long courseId, Long chapterId, Long lessonId, LessonUpdateReq req);
    void softDeleteLesson(Long courseId, Long chapterId, Long lessonId);
    LessonSearchRes getLessons(LessonSearchReq req);
}
