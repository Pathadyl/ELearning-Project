package com.dylan.elearning.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import com.dylan.elearning.constant.ConfigConstant;
import com.dylan.elearning.dto.request.admin.LessonCreateReq;
import com.dylan.elearning.dto.request.admin.LessonSearchReq;
import com.dylan.elearning.dto.request.admin.LessonUpdateReq;
import com.dylan.elearning.dto.response.admin.LessonResDto;
import com.dylan.elearning.dto.response.admin.LessonSearchRes;
import com.dylan.elearning.entity.Chapter;
import com.dylan.elearning.entity.Lesson;
import com.dylan.elearning.exception.AppException;
import com.dylan.elearning.repository.LessonRepository;
import com.dylan.elearning.service.ILessonService;
import com.dylan.elearning.util.DateUtils;
import com.dylan.elearning.util.PageableUtils;
import com.dylan.elearning.util.ValidateUtils;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class LessonServiceImpl implements ILessonService {

    private static final Logger log = LoggerFactory.getLogger(ILessonService.class);

    @Autowired
    private LessonRepository lessonRepository;

    @Autowired
    private ValidateUtils validateUtils;

    @Override
    public List<LessonResDto> addLessons(Long courseId, Long chapterId, List<LessonCreateReq> req) {
        // validateRequest
        Chapter chapter = validateUtils.validateCourseAndChapter(courseId, chapterId);

        List<Lesson> lessons = req.stream().map(createReq -> {
            Lesson lesson = new Lesson();
            BeanUtils.copyProperties(createReq, lesson);
            lesson.setStatus(ConfigConstant.ACTIVE.getValue());
            lesson.setChapter(chapter);
            lesson.setCreatedDate(new Date());
            return lesson;
        }).collect(Collectors.toList());

        List<Lesson> savedLesson = lessonRepository.saveAll(lessons);
        List<LessonResDto> res = savedLesson.stream().map(lesson -> {
            LessonResDto lessonResDto = new LessonResDto();
            BeanUtils.copyProperties(lesson, lessonResDto);
            lessonResDto.setChapterId(lesson.getChapter().getId());
            lessonResDto.setCreatedDate(DateUtils.formatDate(lesson.getCreatedDate()));
            return lessonResDto;
        }).collect(Collectors.toList());

        return res;
    }

    @Override
    public LessonResDto updateLesson(Long courseId, Long chapterId, Long lessonId, LessonUpdateReq req) {
        // validateRequest
        Chapter chapter = validateUtils.validateCourseAndChapter(courseId, chapterId);
        if (req.getStatus() != null && (ConfigConstant.INACTIVE.getCode() != req.getStatus()
                && ConfigConstant.ACTIVE.getCode() != req.getStatus())) {
            throw new AppException("Status must be 0 (inactive) or 1 (active)");
        }

        Lesson lesson = lessonRepository.getLessonById(lessonId)
                .orElseThrow(() -> new AppException("Lesson not found with id: " + lessonId));

        if (!chapter.getId().equals(lesson.getChapter().getId())) {
            throw new AppException("Lesson does not belong to the specified chapter");
        }

        if (req.getStatus() != null) {
            String newStatus = (req.getStatus() == ConfigConstant.INACTIVE.getCode())
                    ? ConfigConstant.INACTIVE.getValue()
                    : ConfigConstant.ACTIVE.getValue();

            if (!newStatus.equals(lesson.getStatus())) lesson.setStatus(newStatus);
        }
        Optional.ofNullable(req.getName()).ifPresent(lesson::setName);
        Optional.ofNullable(req.getType()).ifPresent(lesson::setType);
        Optional.ofNullable(req.getUrl()).ifPresent(lesson::setUrl);
        Optional.ofNullable(req.getDescription()).ifPresent(lesson::setDescription);
        lesson.setUpdatedDate(new Date());

        lessonRepository.save(lesson);

        LessonResDto res = new LessonResDto();
        BeanUtils.copyProperties(lesson, res);

        return res;
    }

    @Override
    public void softDeleteLesson(Long courseId, Long chapterId, Long lessonId) {
        Lesson lesson = lessonRepository.getLessonById(lessonId)
                .orElseThrow(() -> new AppException("Lesson not found with id: " + lessonId));

        lesson.setStatus(ConfigConstant.INACTIVE.getValue());
        lesson.setUpdatedDate(new Date());
        lessonRepository.save(lesson);

        log.info("Soft delete lesson with id: {}", lessonId);
    }

    @Override
    public LessonSearchRes getLessons(LessonSearchReq req) {
        req.setPageIndex(req.getPageIndex() != null && req.getPageIndex() >= 0 ? req.getPageIndex() : 0);
        req.setPageSize(req.getPageSize() != null && req.getPageSize() >= 1 ? req.getPageSize() : 10);
        //
        Sort sort = PageableUtils.determineSort(req.getSort());
        Pageable pageable = PageRequest.of(req.getPageIndex(), req.getPageSize(), sort);

        Page<Lesson> lessonPage = lessonRepository.findLessons(
                req.getName(),
                req.getStatus(),
                req.getCourseId(),
                req.getChapterId(),
                req.getType(),
                DateUtils.stringToDate(req.getCreatedDateFrom()),
                DateUtils.stringToDate(req.getCreatedDateTo()),
                pageable
        );

        List<LessonResDto> listLessonRes = lessonPage.getContent().stream()
                .map(lesson -> {
                    LessonResDto lessonResDto = new LessonResDto();
                    BeanUtils.copyProperties(lesson, lessonResDto);
                    return lessonResDto;
                }).collect(Collectors.toList());

        LessonSearchRes res = new LessonSearchRes();
        res.setLessons(listLessonRes);
        res.setTotalItems(lessonPage.getTotalElements());
        return res;
    }
}
