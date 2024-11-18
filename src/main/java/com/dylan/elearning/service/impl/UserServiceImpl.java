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
import com.dylan.elearning.constant.StatusConstant;
import com.dylan.elearning.dto.request.user.*;
import com.dylan.elearning.dto.response.admin.UserSearchRes;
import com.dylan.elearning.dto.response.user.*;
import com.dylan.elearning.dto.response.user.DetailResponse.*;
import com.dylan.elearning.entity.*;
import com.dylan.elearning.entity.CompositeKey.*;
import com.dylan.elearning.exception.CourseException.CourseInactiveException;
import com.dylan.elearning.exception.CourseException.CourseNotFoundException;
import com.dylan.elearning.exception.LessonException.LessonNotFoundException;
import com.dylan.elearning.exception.LessonException.NoLessonInCourseException;
import com.dylan.elearning.exception.RequestException;
import com.dylan.elearning.exception.UserException.*;
import com.dylan.elearning.repository.*;
import com.dylan.elearning.service.IUserService;
import com.dylan.elearning.util.*;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements IUserService {
    private static final Logger log = LoggerFactory.getLogger(IUserService.class);
    private final UserRepository userRepository;
    private final FullNameRepository fullNameRepository;
    private final AddressRepository addressRepository;
    private final CourseRepository courseRepository;
    private final LessonRepository lessonRepository;
    private final UserCourseRepository userCourseRepository;
    private final UserCourseLessonRepository userCourseLessonRepository;

    @Autowired
    public UserServiceImpl(
            UserRepository userRepository,
            FullNameRepository fullNameRepository,
            AddressRepository addressRepository,
            CourseRepository courseRepository,
            LessonRepository lessonRepository,
            UserCourseRepository userCourseRepository,
            UserCourseLessonRepository userCourseLessonRepository
    ) {
        this.userRepository = userRepository;
        this.fullNameRepository = fullNameRepository;
        this.addressRepository = addressRepository;
        this.courseRepository = courseRepository;
        this.lessonRepository = lessonRepository;
        this.userCourseRepository = userCourseRepository;
        this.userCourseLessonRepository = userCourseLessonRepository;
    }

    @Override
    public UserResponseDto register(UserRegisterReq req) {
        // Check if username has already existed or not
        if (userRepository.findUserByUsername(req.getUsername()).isPresent()) {
            throw new UsernameExistException("Username has already existed!");
        }
        //
        User user = new User();
        BeanUtils.copyProperties(req, user);
        user.setCreatedDate(new Date());
        user.setStatus(ConfigConstant.ACTIVE.getValue());
        User savedUser = userRepository.save(user);
        //
        FullName fullName = new FullName();
        BeanUtils.copyProperties(req, fullName);
        fullName.setCreatedDate(new Date());
        fullName.setStatus(ConfigConstant.ACTIVE.getValue());
        fullName.setUser(savedUser);
        FullName savedFullname = fullNameRepository.save(fullName);
        //
        List<Address> addresses = new ArrayList<>();
        for(AddressRequest addressRequest : req.getAddressCreateRequests()) {
            Address address = new Address();
            BeanUtils.copyProperties(addressRequest, address);
            Address savedAddress = addressRepository.save(address);
            addresses.add(savedAddress);
        }
        //
        savedUser.setAddress(addresses);
        savedUser.setFullName(savedFullname);
        savedUser = userRepository.save(savedUser);

        return convertToUserResponseDto(savedUser);
    }

    @Override
    public UserResponseDto login(UserLoginReq req) {
        // Validate if the username correct and active
        User user = userRepository.findActiveUserByUsername(req.getUsername())
                .orElseThrow(() -> new UserNotFoundException("Incorrect username!"));
        // Validate if the password correct
        if(!user.getPassword().equals(req.getPassword()))
            throw new InvalidPasswordException("Incorrect password!");

        return convertToUserResponseDto(user);
    }

    @Override
    public UserResponseDto updateUser(Long userId, UserUpdateReq req) {
        // Validate if user existed with given id
        User user = validateUserExist(userId);
        // Validate request
        checkConfigStatusRequest(req.getStatus());
        //
        FullName fullName = user.getFullName();
        BeanUtils.copyProperties(req, fullName);
        //
        if (req.getStatus() != null) {
            String newStatus = (req.getStatus() == ConfigConstant.INACTIVE.getCode())
                    ? ConfigConstant.INACTIVE.getValue()
                    : ConfigConstant.ACTIVE.getValue();
            if(!newStatus.equals(user.getStatus())) {
                user.setStatus(newStatus);
                fullName.setStatus(newStatus);
            }
        }
        //
        fullName.setUpdatedDate(new Date());
        FullName savedFullname = fullNameRepository.save(fullName);
        //
        for(int i = 0; i < req.getAddressUpdateRequests().size(); i++) {
            BeanUtils.copyProperties(req.getAddressUpdateRequests().get(i), user.getAddress().get(i));
            addressRepository.save(user.getAddress().get(i));
        }
        //
        BeanUtils.copyProperties(req, user);
        user.setFullName(savedFullname);
        user.setUpdatedDate(new Date());
        User updateUser = userRepository.save(user);

        return convertToUserResponseDto(updateUser);
    }

    @Override
    public void deleteUser(Long userId) {
        // Validate user existed and user is active
        User user = validateUserExist(userId);
        checkActiveUser(user);
        //
        user.setStatus(ConfigConstant.INACTIVE.getValue());
        user.getFullName().setStatus(ConfigConstant.INACTIVE.getValue());
        user.setUpdatedDate(new Date());
        user.getFullName().setUpdatedDate(new Date());
        userRepository.save(user);
        fullNameRepository.save(user.getFullName());
        //
        log.info("Soft delete user with id: {}", userId);
    }

    @Override
    public UserSearchRes getUsers(UserSearchReq req) {
        //
        req.setPageIndex(req.getPageIndex() != null && req.getPageIndex() >= 0 ? req.getPageIndex() : 0);
        req.setPageSize(req.getPageSize() != null && req.getPageSize() >= 1 ? req.getPageSize() : 10);
        //
        Sort sort = PageableUtils.determineSort(req.getSort());
        Pageable pageable = PageRequest.of(req.getPageIndex(), req.getPageSize(), sort);
        //
        Page<User> userPage = userRepository.findUsers(
                req.getName(),
                req.getUsername(),
                req.getStatus(),
                req.getCreatedDateFrom(),
                req.getCreatedDateTo(),
                pageable
        );
        //
        List<UserResponseDto> listUserRes = userPage.getContent().stream()
                .map(this::convertToUserResponseDto)
                .toList();
        //
        UserSearchRes res = new UserSearchRes();
        res.setUsers(listUserRes);
        res.setTotalItems(userPage.getTotalElements());

        return res;
    }

    @Override
    public UserEnrollCourseRes enrollCourse(Long userId, Long courseId) {
        // Validate user
        User user = validateUserExist(userId); checkActiveUser(user);
        // Validate course
        Course course = validateCourseExist(courseId); validateActiveCourse(course);
        // Validate if user has already enrolled course
        checkIfUserAlreadyEnrolledCourse(user, course);
        //
        UserCourseId userCourseId = new UserCourseId(userId, courseId);
        //
        UserCourse sc = new UserCourse();
        sc.setUserCourseId(userCourseId);
        sc.setUser(user);
        sc.setCourse(course);
        sc.setCreatedDate(new Date());
        sc.setStatus(StatusConstant.START.getValue());
        //
        UserCourse savedSc = userCourseRepository.save(sc);
        //
        for(Chapter ch : savedSc.getCourse().getChapters()) {
            for(Lesson l : ch.getLessons()) {
                UserCourseLesson scl = new UserCourseLesson();
                UserCourseLessonId id = new UserCourseLessonId(
                        savedSc.getUserCourseId().getUserId(),
                        savedSc.getUserCourseId().getCourseId(),
                        l.getId());

                scl.setId(id);
                scl.setUser(user);
                scl.setCourse(course);
                scl.setLesson(l);
                scl.setStatus(StatusConstant.START.getValue());
                scl.setCreatedDate(new Date());
                userCourseLessonRepository.save(scl);
            }
        }
        //
        UserEnrollCourseRes res = new UserEnrollCourseRes();
        BeanUtils.copyProperties(savedSc, res);
        res.setUserId(userId);
        res.setCourseId(courseId);
        res.setCreatedDate(DateUtils.formatDateTime(savedSc.getCreatedDate()));
        //
        log.info("User with id: {} has successfully enrolled in course with id: {}", userId, courseId);

        return res;
    }

    @Override
    public UserRateCourseRes rateCourse(Long userId, Long courseId, UserRateCourseReq req) {
        // Validate user
        User user = validateUserExist(userId); checkActiveUser(user);
        // Validate course
        Course course = validateCourseExist(courseId); validateActiveCourse(course);
        // Validate if user has enrolled course
        UserCourse uc = validateUserEnrolledCourse(user, course);
        //
        uc.setRating(req.getRate());
        uc.setUpdatedDate(new Date());

        UserCourse savedSc = userCourseRepository.save(uc);

        UserRateCourseRes res = new UserRateCourseRes();
        res.setUserId(userId);
        res.setCourseId(courseId);
        res.setRate(req.getRate());
        res.setUpdatedDate(DateUtils.formatDateTime(savedSc.getUpdatedDate()));

        log.info("User with id: {} has rated course with id: {}", userId, courseId);

        return res;
    }

    @Override
    public UserReviewCourseRes reviewCourse(Long userId, Long courseId, UserReviewCourseReq req) {
        // Validate user
        User user = validateUserExist(userId); checkActiveUser(user);
        // Validate course
        Course course = validateCourseExist(courseId); validateActiveCourse(course);
        // Validate if uer has enrolled course
        UserCourse uc = validateUserEnrolledCourse(user, course);

        uc.setReview(req.getReview());
        uc.setUpdatedDate(new Date());

        UserCourse savedSc = userCourseRepository.save(uc);

        UserReviewCourseRes res = new UserReviewCourseRes();
        res.setUserId(userId);
        res.setCourseId(courseId);
        res.setReview(req.getReview());
        res.setUpdatedDate(DateUtils.formatDateTime(savedSc.getUpdatedDate()));

        log.info("User with id: {} has review course with id: {}", userId, courseId);

        return res;
    }

    @Override
    public CourseResponseDto getCourseInfo(Long courseId) {
        // Validate course
        Course course = validateCourseExist(courseId);

        return convertToCourseResponseDto(course);
    }

    @Override
    public UserSearchCourseRes getRegisterCourse(UserSearchCourseReq req) {
        // Validate user
        validateUserExist(req.getUserId());
        //
        req.setPageIndex(req.getPageIndex() != null && req.getPageIndex() >= 0 ? req.getPageIndex() : 0);
        req.setPageSize(req.getPageSize() != null && req.getPageSize() >= 1 ? req.getPageSize() : 10);
        //
        Sort sort = PageableUtils.determineSort(req.getSort());
        Pageable pageable = PageRequest.of(req.getPageIndex(), req.getPageSize(), sort);
        //
        Page<Course> coursePage = userRepository.findRegisterCourses(
                req.getUserId(),
                req.getName(),
                req.getStatus(),
                req.getTeacherName(),
                req.getCreatedDateFrom(),
                req.getCreatedDateFrom(),
                req.getRatingFrom(),
                req.getRatingTo(),
                req.getNumLessons(),
                pageable
        );
        //
        List<Course> courses = coursePage.getContent();
        List<CourseRegisterSearchResponse> listCourseRegisterRes = courses.stream()
                .map(course -> convertToRegisterCourseResponseDto(req.getUserId(), course))
                .toList();
        //
        UserSearchCourseRes res = new UserSearchCourseRes();
        res.setRegisterCourses(listCourseRegisterRes);
        res.setTotalItems(coursePage.getTotalElements());

        return res;
    }

    @Override
    public UserStudyRes study(Long userId, Long courseId, Long lessonId, UserStudyReq req) {
        // Validate user
        User user = validateUserExist(userId); checkActiveUser(user);
        // Validate course
        Course course = validateCourseExist(courseId); validateActiveCourse(course);
        // Validate if user has enrolled course
        UserCourse uc = validateUserEnrolledCourse(user, course);
        // Validate lesson
        Lesson lesson = validateLessonExist(lessonId); validateActiveLesson(lesson);
        //Validate if course has corresponding lesson
        checkIfCourseHasLesson(course, lesson);
        // Validate status request
        checkStatusRequest(req.getStatus());
        //
        UserCourseLesson ucl = userCourseLessonRepository.getUserCourseLesson(uc, lesson)
                .orElseThrow(() -> new NoLessonInCourseException(
                        "Record with user id: " + uc.getUser().getId() +
                                " and course id: " + uc.getCourse().getId() +
                                " and lesson id: " + lesson.getId() +
                                " does not exist!"));
        ucl.setStatus(req.getStatus());
        ucl.setUpdatedDate(new Date());
        //
        UserCourseLesson savedUcl = userCourseLessonRepository.save(ucl);
        //
        if (checkIfCompleteAllLesson(savedUcl.getUserCourse())
        && !savedUcl.getUserCourse().getStatus().equals(StatusConstant.DONE.getValue())) {
            savedUcl.getUserCourse().setStatus(StatusConstant.DONE.getValue());
            userCourseRepository.save(ucl.getUserCourse());
        } else if (checkIfUserProcessingCourse(savedUcl.getUserCourse())
        && !savedUcl.getUserCourse().getStatus().equals(StatusConstant.PROCESSING.getValue())) {
            savedUcl.getUserCourse().setStatus(StatusConstant.PROCESSING.getValue());
            userCourseRepository.save(ucl.getUserCourse());
        }
            UserStudyRes res = new UserStudyRes();
            res.setUserId(userId);
            res.setCourseId(courseId);
            res.setLessonId(lessonId);
            res.setStatus(req.getStatus());
            res.setUpdatedDate(DateUtils.formatDateTime(savedUcl.getUpdatedDate()));

            return res;
        }

    private User validateUserExist(Long userId) {
        return userRepository.getUserById(userId)
                .orElseThrow(() -> new UserNotFoundException("Not found student with id: " + userId));
    }

    private void checkActiveUser(User user) {
        if(user.getStatus().equals(ConfigConstant.INACTIVE.getValue()))
            throw new UserInactiveException("User is now unavailable!");

    }

    private Course validateCourseExist(Long courseId) {
        return courseRepository.getCourseById(courseId)
                .orElseThrow(() -> new CourseNotFoundException("Not found course with id: " + courseId));
    }

    private void validateActiveCourse(Course course) {
        if(course.getStatus().equals(ConfigConstant.INACTIVE.getValue()))
            throw new CourseInactiveException("Course is now unavailable!");
    }

    private Lesson validateLessonExist(Long lessonId) {
        return lessonRepository.getLessonById(lessonId)
                .orElseThrow(
                        () -> new LessonNotFoundException("Not found lesson with id: " + lessonId));
    }

    public void validateActiveLesson(Lesson lesson) {
        if(lesson.getStatus().equals(ConfigConstant.INACTIVE.getValue()))
            throw new CourseInactiveException("Lesson is now unavailable!");
    }

    private void checkIfUserAlreadyEnrolledCourse(User user, Course course) {
        if(userCourseRepository.getUserCourse(user, course).isPresent())
            throw new UserAlreadyEnrolledException("User with id: " + user.getId() +
                    " is already enrolled in course with id: " + course.getId());

    }

    private UserCourse validateUserEnrolledCourse(User user, Course course) {
        return userCourseRepository.getUserCourse(user, course)
                .orElseThrow(() -> new UserNotYetEnrolledException("User with id: " + user.getId() +
                        " has not enrolled course with id: " + course.getId() + " yet!"));
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

    private boolean checkIfCompleteAllLesson(UserCourse uc) {
        return userCourseLessonRepository.getListUserCourseLesson(uc).stream()
                .allMatch(ucl -> StatusConstant.DONE.getValue().equals(ucl.getStatus()));
    }

    private boolean checkIfUserProcessingCourse(UserCourse uc) {
        return !userCourseLessonRepository.getListUserCourseLesson(uc).stream()
                .allMatch(ucl -> StatusConstant.START.getValue().equals(ucl.getStatus()));
    }

    private void checkConfigStatusRequest(Integer status) {
        if(status != null
                && status != ConfigConstant.ACTIVE.getCode()
                && status != ConfigConstant.INACTIVE.getCode())
            throw new RequestException("Status must be active or inactive!");
    }

    private void checkStatusRequest(String status) {
        if(!Objects.equals(status, StatusConstant.START.getValue())
                && !Objects.equals(status, StatusConstant.PROCESSING.getValue())
                && !Objects.equals(status, StatusConstant.DONE.getValue()))
            throw new RequestException("Status must be START, PROCESSING or DONE!");
    }

    private UserResponseDto convertToUserResponseDto(User user) {

        UserResponseDto userResponseDto = new UserResponseDto();
        userResponseDto.setFullName(
                user.getFullName() != null ? convertToFullNameResponseDto(user.getFullName()) : null
        );

        userResponseDto.setAddresses( user.getAddress() != null ?
                user.getAddress().stream()
                        .map(this::convertToAddressResponseDto)
                        .collect(Collectors.toList()) : null);

        userResponseDto.setUsername(user.getUsername());
        userResponseDto.setPassword(user.getPassword());
        userResponseDto.setStatus(user.getStatus());
        userResponseDto.setCreatedDate(DateUtils.formatDateTime(user.getCreatedDate()));
        userResponseDto.setUpdatedDate(
                user.getUpdatedDate() != null ? DateUtils.formatDateTime(user.getUpdatedDate()) : null
        );
        userResponseDto.setRegisterCourses(user.getUserCourses().size());

        return userResponseDto;
    }

    private CourseResponseDto convertToCourseResponseDto(Course course) {

        List<ChapterResponseDto> chapters = course.getChapters().stream()
                .map(this::convertToChapterResponseDto)
                .collect(Collectors.toList());

        CourseResponseDto courseDto = new CourseResponseDto(
                course.getName(),
                course.getDescription(),
                course.getStatus(),
                chapters,
                convertToTeacherResponseDto(course.getTeacher())
        );
        courseDto.setCreatedDate(DateUtils.formatDateTime(course.getCreatedDate()));
        courseDto.setUpdatedDate(
                course.getUpdatedDate() != null ? DateUtils.formatDateTime(course.getUpdatedDate()) : null);

        return courseDto;
    }

    private CourseRegisterSearchResponse convertToRegisterCourseResponseDto(Long userId, Course course) {

        CourseRegisterSearchResponse courseRegisterSearchResponse
                = new CourseRegisterSearchResponse();

        BeanUtils.copyProperties(course, courseRegisterSearchResponse);
        courseRegisterSearchResponse.setTeacherName(course.getTeacher().getName());
        courseRegisterSearchResponse.setCreatedDate(DateUtils.formatDateTime(course.getCreatedDate()));
        courseRegisterSearchResponse.setUpdatedDate(
                course.getUpdatedDate() != null ? DateUtils.formatDateTime(course.getUpdatedDate()) : null);
        courseRegisterSearchResponse.setNumChapters(course.getChapters().size());
        courseRegisterSearchResponse.setNumLessons(
                course.getChapters().stream()
                        .mapToInt(ch -> ch.getLessons().size())
                        .sum()
        );
        courseRegisterSearchResponse.setRate(
                course.getUserCourses().stream()
                        .filter(userCourse -> Objects.equals(userCourse.getUser().getId(), userId))
                        .map(UserCourse::getRating)
                        .findFirst()
                        .orElse(null)
        );


        return courseRegisterSearchResponse;

    }

    private ChapterResponseDto convertToChapterResponseDto(Chapter chapter) {

        ChapterResponseDto chapterDto = new ChapterResponseDto();
        chapterDto.setName(chapter.getName());
        chapterDto.setDescription(chapter.getDescription());
        chapterDto.setOrder(chapter.getOrder());
        List<LessonResponseDto> lessons = chapter.getLessons().stream()
                .map(this::convertToLessonResponseDto)
                .collect(Collectors.toList());
        chapterDto.setLessons(lessons);


        return chapterDto;
    }

    private LessonResponseDto convertToLessonResponseDto(Lesson lesson) {

        LessonResponseDto lessonDto = new LessonResponseDto();
        lessonDto.setName(lesson.getName());
        lessonDto.setDescription(lesson.getDescription());
        lessonDto.setType(lesson.getType());
        lessonDto.setUrl(lesson.getUrl());
        lessonDto.setOrder(lesson.getOrder());

        return lessonDto;
    }

    private TeacherResponseDto convertToTeacherResponseDto(Teacher teacher) {

        TeacherResponseDto teacherDto = new TeacherResponseDto();
        teacherDto.setName(teacher.getName());

        return teacherDto;
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

    private FullNameResponseDto convertToFullNameResponseDto(FullName fullName) {

        FullNameResponseDto fullNameResponseDto = new FullNameResponseDto();
        fullNameResponseDto.setFullName(
                fullName.getLastName() + " " +
                        fullName.getMidName() + " " +
                        fullName.getFirstName());

        return fullNameResponseDto;
    }

}
