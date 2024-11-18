package com.dylan.elearning.service.impl;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.dylan.elearning.constant.StatusConstant;
import com.dylan.elearning.dto.request.admin.AdminLoginReq;
import com.dylan.elearning.dto.response.admin.*;
import com.dylan.elearning.entity.*;
import com.dylan.elearning.exception.AdminException.AdminNotFoundException;
import com.dylan.elearning.exception.ChapterException.ChapterNotFoundException;
import com.dylan.elearning.exception.CourseException.CourseNotFoundException;
import com.dylan.elearning.exception.LessonException.LessonNotFoundException;
import com.dylan.elearning.exception.LessonException.NoLessonInCourseException;
import com.dylan.elearning.exception.NoRecordFoundException;
import com.dylan.elearning.exception.TeacherException.TeacherNotFoundException;
import com.dylan.elearning.exception.UserException.InvalidPasswordException;
import com.dylan.elearning.exception.UserException.UserNotFoundException;
import com.dylan.elearning.exception.UserException.UserNotYetEnrolledException;
import com.dylan.elearning.repository.*;
import com.dylan.elearning.service.IAdminService;
import com.dylan.elearning.util.*;

import java.util.List;

@Service
public class AdminServiceImpl implements IAdminService {
    private final AdminRepository adminRepository;
    private final UserRepository userRepository;
    private final CourseRepository courseRepository;
    private final ChapterRepository chapterRepository;
    private final LessonRepository lessonRepository;
    private final TeacherRepository teacherRepository;
    private final UserCourseRepository userCourseRepository;
    private final UserCourseLessonRepository userCourseLessonRepository;

    @Autowired
    public AdminServiceImpl(
            AdminRepository adminRepository,
            UserRepository userRepository,
            CourseRepository courseRepository,
            ChapterRepository chapterRepository,
            LessonRepository lessonRepository,
            TeacherRepository teacherRepository,
            UserCourseRepository userCourseRepository,
            UserCourseLessonRepository userCourseLessonRepository
    ) {
        this.adminRepository = adminRepository;
        this.userRepository = userRepository;
        this.courseRepository = courseRepository;
        this.chapterRepository = chapterRepository;
        this.lessonRepository = lessonRepository;
        this.teacherRepository = teacherRepository;
        this.userCourseRepository = userCourseRepository;
        this.userCourseLessonRepository = userCourseLessonRepository;
    }

    @Override
    public AdminResponseDto login(AdminLoginReq req) {
        // Validate admin
        Admin admin = validateLoginUsername(req.getUsername());
        // Validate password
        checkLoginPassword(admin, req.getPassword());

        return convertToAdminResponseDto(admin);
    }

    @Override
    public UserResponseDto getUserInfo(Long userId) {
        // Validate user
        User user = validateUserExist(userId);

        return convertToUserResponseDto(user);
    }

    public UserCourseInfoRes getUserCourseInfo(Long userId, Long courseId) {
        // Validate user
        User user = validateUserExist(userId);
        // Validate course
        Course course = validateCourseExist(courseId);
        // Validate if user has enrolled course
        UserCourse uc = validateUserEnrolledCourse(user, course);
        //
        List<UserCourseLesson> userCourseLessonList = userCourseLessonRepository.getListUserCourseLesson(uc);
        //
        UserCourseInfoRes res = new UserCourseInfoRes();
        res.setUser(convertToUserResponseDto(user));
        res.setCourse(convertToCourseResponseDto(course));
        res.setRate(
                uc.getRating() != null ? uc.getRating() : null
        );
        res.setReview(
                uc.getReview() != null ? uc.getReview() : null
        );
        res.setCreatedDate(DateUtils.formatDateTime(uc.getCreatedDate()));
        res.setUpdatedDate(
                uc.getUpdatedDate() != null ? DateUtils.formatDateTime(uc.getUpdatedDate()) : null
        );
        res.setNumLessonProcessing((int) userCourseLessonList.stream()
                .filter(scl -> StatusConstant.PROCESSING.getValue().equals(scl.getStatus()))
                .count());
        res.setNumLessonDone((int) userCourseLessonList.stream()
                .filter(scl -> StatusConstant.DONE.getValue().equals(scl.getStatus()))
                .count());

        return res;
    }

    @Override
    public UserCourseLessonInfoRes getUserCourseLessonInfo(Long userId, Long courseId, Long lessonId) {
        // Validate user
        User user = validateUserExist(userId);
        // Validate course
        Course course = validateCourseExist(courseId);
        // Validate if user has enrolled course
        UserCourse uc = validateUserEnrolledCourse(user, course);
        // Validate lesson
        Lesson lesson = validateLessonExist(lessonId);
        // Validate if course has corresponding lesson
        checkIfCourseHasLesson(course, lesson);
        //
        UserCourseLesson ucl = userCourseLessonRepository.getUserCourseLesson(uc, lesson)
                .orElseThrow(() -> new NoRecordFoundException(
                        "Record with user id: " + uc.getUser().getId() +
                                " and course id: " + uc.getCourse().getId() +
                                " and lesson id: " + lesson.getId() +
                                " does not exist!"));
        //
        UserCourseLessonInfoRes res = new UserCourseLessonInfoRes();
        res.setUser(convertToUserResponseDto(user));
        res.setCourse(convertToCourseResponseDto(course));
        res.setLesson(convertToLessonResponseDto(lesson));
        res.setStatus(ucl.getStatus());
        res.setCreatedDate(DateUtils.formatDateTime(ucl.getCreatedDate()));
        res.setUpdatedDate(
                ucl.getUpdatedDate() != null ? DateUtils.formatDateTime(ucl.getUpdatedDate()) : null
        );

        return res;
    }

    @Override
    public TeacherInfoRes getTeacherInfo(Long teacherId) {
        // Validate teacher
        Teacher teacher = validateTeacherExist(teacherId);
        //
        TeacherInfoRes res = new TeacherInfoRes();
        res.setTeacher(convertToTeacherResponseDto(teacher));
        res.setNumCourseTeach(teacher.getCourses().size());

        return res;
    }

    @Override
    public CourseInfoRes getCourseInfo(Long courseId) {
        // Validate course
        Course course = validateCourseExist(courseId);
        //
        CourseInfoRes res = new CourseInfoRes();
        res.setCourse(convertToCourseResponseDto(course));
        res.setNumChapter(course.getChapters().size());
        res.setNumLesson(course.getChapters().stream()
                .mapToInt(ch -> ch.getLessons().size())
                .sum());
        res.setNumRegisterUser(course.getUserCourses().size());

        return res;
    }

    @Override
    public ChapterInfoRes getChapterInfo(Long chapterId) {
        // Validate chapter
        Chapter chapter = validateChapterExist(chapterId);
        //
        ChapterInfoRes res = new ChapterInfoRes();
        res.setChapter(convertToChapterResponseDto(chapter));
        res.setCourse(convertToCourseResponseDto(chapter.getCourse()));
        res.setNumLesson(chapter.getLessons().size());

        return res;
    }

    @Override
    public LessonInfoRes getLessonInfo(Long lessonId) {
        // Validate lesson
        Lesson lesson = validateLessonExist(lessonId);
        //
        LessonInfoRes res = new LessonInfoRes();
        res.setCourse(convertToCourseResponseDto(lesson.getChapter().getCourse()));
        res.setChapter(convertToChapterResponseDto(lesson.getChapter()));
        res.setLesson(convertToLessonResponseDto(lesson));
        res.setNumUserProcessing((int) lesson.getUserCourseLessons().stream()
                .filter(scl -> StatusConstant.PROCESSING.getValue().equals(scl.getStatus()))
                .count()
        );
        res.setNumUserDone((int) lesson.getUserCourseLessons().stream()
                .filter(scl -> StatusConstant.DONE.getValue().equals(scl.getStatus()))
                .count()
        );

        return res;
    }

    private Admin validateLoginUsername(String username) {
        return adminRepository.findActiveAdminByUsername(username)
                .orElseThrow(() -> new AdminNotFoundException("Incorrect username!"));
    }

    private void checkLoginPassword(Admin admin, String password) {
        if(!admin.getPassword().equals(password))
            throw new InvalidPasswordException("Incorrect password!");
    }

    private User validateUserExist(Long userId) {
        return userRepository.getUserById(userId)
                .orElseThrow(() -> new UserNotFoundException("Not found student with id: " + userId));
    }

    private Course validateCourseExist(Long courseId) {
        return courseRepository.getCourseById(courseId)
                .orElseThrow(() -> new CourseNotFoundException("Not found course with id: " + courseId));
    }

    private Chapter validateChapterExist(Long chapterId) {
        return chapterRepository.getChapterById(chapterId)
                .orElseThrow(() -> new ChapterNotFoundException("Not found chapter with id: " + chapterId));
    }

    private Lesson validateLessonExist(Long lessonId) {
        return lessonRepository.getLessonById(lessonId)
                .orElseThrow(() -> new LessonNotFoundException("Not found lesson with id: " + lessonId));
    }

    private Teacher validateTeacherExist(Long teacherId) {
        return teacherRepository.getTeacherById(teacherId)
                .orElseThrow(() -> new TeacherNotFoundException("Not found teacher with id: " + teacherId));
    }

    private void checkIfCourseHasLesson(Course course, Lesson lesson) {
        for(Chapter ch : course.getChapters()) {
            if(ch.getLessons().contains(lesson))
                return;
        }
        throw new NoLessonInCourseException(
                "Course with id: " + course.getId() +
                        " does not have lesson with id: " + lesson.getId());
    }

    private UserCourse validateUserEnrolledCourse(User user, Course course) {
        return userCourseRepository.getUserCourse(user, course)
                .orElseThrow(() -> new UserNotYetEnrolledException("User with id: " + user.getId() +
                        " has not enrolled course with id: " + course.getId() + " yet!"));
    }

    private AdminResponseDto convertToAdminResponseDto(Admin admin) {

        AdminResponseDto adminResponseDto = new AdminResponseDto();
        BeanUtils.copyProperties(admin, adminResponseDto);
        adminResponseDto.setCreatedDate(DateUtils.formatDateTime(admin.getCreatedDate()));
        adminResponseDto.setUpdatedDate(
                admin.getUpdatedDate() != null ? DateUtils.formatDateTime(admin.getUpdatedDate()) : null
        );

        return adminResponseDto;

    }

    private UserResponseDto convertToUserResponseDto(User user) {
        UserResponseDto userResponseDto = new UserResponseDto();
        BeanUtils.copyProperties(user, userResponseDto);
        userResponseDto.setFullName(convertToFullNameResponseDto(user.getFullName()));
        userResponseDto.setAddresses(
                user.getAddress().stream()
                        .map(this::convertToAddressResponseDto)
                        .toList()
        );
        userResponseDto.setCreatedDate(DateUtils.formatDateTime(user.getCreatedDate()));
        userResponseDto.setUpdatedDate(
                user.getUpdatedDate() != null ? DateUtils.formatDateTime(user.getUpdatedDate()) : null
        );
        userResponseDto.setNumCourseRegister(user.getUserCourses().size());

        return userResponseDto;
    }

    private TeacherResDto convertToTeacherResponseDto(Teacher teacher) {

        TeacherResDto teacherResDto = new TeacherResDto();
        BeanUtils.copyProperties(teacher, teacherResDto);
        teacherResDto.setCreatedDate(DateUtils.formatDateTime(teacher.getCreatedDate()));
        teacherResDto.setUpdatedDate(
                teacher.getUpdatedDate() != null ? DateUtils.formatDateTime(teacher.getUpdatedDate()) : null
        );

        return teacherResDto;
    }

    private FullNameResponseDto convertToFullNameResponseDto(FullName fullName) {
        FullNameResponseDto fullNameResponseDto = new FullNameResponseDto();
        fullNameResponseDto.setFullName(
                fullName.getLastName() + " " +
                        fullName.getMidName() + " " +
                        fullName.getFirstName());

        return fullNameResponseDto;
    }

    private AddressResponseDto convertToAddressResponseDto(Address address) {

        AddressResponseDto addressResponseDto = new AddressResponseDto();
        addressResponseDto.setAddress(
                address.getAddressDetail() + ", " +
                        address.getStreet() + ", " +
                        address.getDistrict() + ", " +
                        address.getCity() + ", " +
                        address.getCountry()
        );

        return addressResponseDto;
    }

    private CourseResponseDto convertToCourseResponseDto(Course course) {
        CourseResponseDto courseResponseDto = new CourseResponseDto();
        BeanUtils.copyProperties(course, courseResponseDto);
        courseResponseDto.setCreatedDate(DateUtils.formatDateTime(course.getCreatedDate()));
        courseResponseDto.setUpdatedDate(
                course.getUpdatedDate() != null ? DateUtils.formatDateTime(course.getUpdatedDate()) : null
        );

        return courseResponseDto;
    }

    private ChapterResponseDto convertToChapterResponseDto(Chapter chapter) {

        ChapterResponseDto chapterResponseDto = new ChapterResponseDto();
        BeanUtils.copyProperties(chapter, chapterResponseDto);
        chapterResponseDto.setCreatedDate(DateUtils.formatDateTime(chapter.getCreatedDate()));
        chapterResponseDto.setUpdatedDate(
                chapter.getUpdatedDate() != null ? DateUtils.formatDateTime(chapter.getUpdatedDate()) : null
        );

        return chapterResponseDto;
    }

    private LessonResDto convertToLessonResponseDto(Lesson lesson) {
        LessonResDto lessonResponseDto = new LessonResDto();
        BeanUtils.copyProperties(lesson, lessonResponseDto);
        lessonResponseDto.setCreatedDate(DateUtils.formatDateTime(lesson.getCreatedDate()));
        lessonResponseDto.setUpdatedDate(
                lesson.getUpdatedDate() != null ? DateUtils.formatDateTime(lesson.getUpdatedDate()) : null
        );

        return lessonResponseDto;
    }
}
