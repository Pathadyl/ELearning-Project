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
import com.dylan.elearning.dto.request.admin.CourseCreateReq;
import com.dylan.elearning.dto.request.admin.CourseSearchReq;
import com.dylan.elearning.dto.request.admin.CourseUpdateReq;
import com.dylan.elearning.dto.response.admin.CourseResponseDto;
import com.dylan.elearning.dto.response.admin.CourseSearchRes;
import com.dylan.elearning.entity.Course;
import com.dylan.elearning.entity.Teacher;
import com.dylan.elearning.exception.AppException;
import com.dylan.elearning.repository.CourseRepository;
import com.dylan.elearning.repository.TeacherRepository;
import com.dylan.elearning.service.ICourseService;
import com.dylan.elearning.util.DateUtils;
import com.dylan.elearning.util.PageableUtils;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CourseServiceImpl implements ICourseService {

    private static final Logger log = LoggerFactory.getLogger(ICourseService.class);

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private TeacherRepository teacherRepository;

    @Override
    public CourseResponseDto createCourse(CourseCreateReq req) {
        Course course = new Course();
        BeanUtils.copyProperties(req, course);
        course.setCreatedDate(new Date());
        course.setStatus(ConfigConstant.ACTIVE.getValue());

        assignTeacher(req.getTeacherId(), course);
        Course savedCourse = courseRepository.save(course);

        CourseResponseDto courseRes = new CourseResponseDto();
        BeanUtils.copyProperties(savedCourse, courseRes);
        courseRes.setTeacherId(savedCourse.getTeacher().getId());
        courseRes.setCreatedDate(DateUtils.formatDate(savedCourse.getCreatedDate()));

        return courseRes;
    }

    @Override
    public CourseResponseDto updateCourse(Long courseId, CourseUpdateReq req) {
        if (req.getStatus() != null && (ConfigConstant.INACTIVE.getCode() != req.getStatus()
                && ConfigConstant.ACTIVE.getCode() != req.getStatus())) {
            throw new AppException("Status must be 0 (inactive) or 1 (active)");
        }

        Course course = courseRepository.getCourseById(courseId)
                .orElseThrow(() -> new AppException("Course not found with id: " + courseId));


        if (req.getStatus() != null) {
            String newStatus = (req.getStatus() == ConfigConstant.INACTIVE.getCode())
                    ? ConfigConstant.INACTIVE.getValue()
                    : ConfigConstant.ACTIVE.getValue();

            if (!newStatus.equals(course.getStatus())) course.setStatus(newStatus);
        }
        Optional.ofNullable(req.getName()).ifPresent(course::setName);
        Optional.ofNullable(req.getDescription()).ifPresent(course::setDescription);
        course.setUpdatedDate(new Date());

        assignTeacher(req.getTeacherId(), course);
        Course savedCourse = courseRepository.save(course);

        CourseResponseDto courseRes = new CourseResponseDto();
        BeanUtils.copyProperties(savedCourse, courseRes);
        courseRes.setTeacherId(savedCourse.getTeacher().getId());
        courseRes.setUpdatedDate(DateUtils.formatDate(savedCourse.getUpdatedDate()));

        return courseRes;
    }

    @Override
    public void softDeleteCourse(Long courseId) {
        Course course = courseRepository.getCourseById(courseId)
                .orElseThrow(() -> new AppException("Course not found with id: " + courseId));

        course.setStatus(ConfigConstant.INACTIVE.getValue());
        course.setUpdatedDate(new Date());
        courseRepository.save(course);

        log.info("Soft delete course with id: {}", courseId);
    }

    @Override
    public CourseSearchRes getCourses(CourseSearchReq req) {
        //
        req.setPageIndex(req.getPageIndex() != null && req.getPageIndex() >= 0 ? req.getPageIndex() : 0);
        req.setPageSize(req.getPageSize() != null && req.getPageSize() >= 1 ? req.getPageSize() : 10);
        //
        Sort sort = PageableUtils.determineSort(req.getSort());
        //
        Pageable pageable = PageRequest.of(req.getPageIndex(), req.getPageSize(), sort);

        Page<Course> coursePage = courseRepository.findCourses(
                req.getName(),
                req.getStatus(),
                req.getTeacherName(),
                DateUtils.stringToDate(req.getCreatedDateFrom()),
                DateUtils.stringToDate(req.getCreatedDateTo()),
                pageable
        );

        List<Course> courses = coursePage.getContent();
        List<CourseResponseDto> listCourseRes = courses.stream().map(course -> {
                    CourseResponseDto courseRes = new CourseResponseDto();
                    BeanUtils.copyProperties(course, courseRes);
                    return courseRes;
                })
                .collect(Collectors.toList());

        CourseSearchRes res = new CourseSearchRes();
        res.setCourses(listCourseRes);
        res.setTotalItems(coursePage.getTotalElements());
        return res;
    }

    private void assignTeacher(Long teacherId, Course course) {
        if (Objects.nonNull(teacherId)) {
            Teacher teacher = teacherRepository.getTeacherById(teacherId)
                    .orElseThrow(() -> new AppException("Teacher not found with id: " + teacherId));

            if (ConfigConstant.INACTIVE.getValue().equals(teacher.getStatus())) {
                throw new AppException("Teacher is already 'inactive'");
            }
            course.setTeacher(teacher);
        }
    }
}
