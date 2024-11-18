package com.dylan.elearning.constant;

public class UrlConstant {

    private UrlConstant() {
    }

    //
    public static final String API_V1 = "/api/v1";
    //
    public static final String USER_LOGIN = "/users/login";
    //
    public static final String DELETE_USERS = "/users/{user_id}";
    //
    public static final String UPDATE_TEACHERS = "/teachers/{teacher_id}";
    //
    public static final String DELETE_TEACHERS = "/teachers/{teacher_id}";
    //
    public static final String ADD_COURSES = "/courses";
    //
    public static final String UPDATE_COURSES = "/courses/{course_id}";
    //
    public static final String DELETE_COURSES = "/courses/{course_id}";
    //
    public static final String ADD_CHAPTERS = "/courses/{course_id}/chapters";
    //
    public static final String UPDATE_CHAPTERS = "/courses/{course_id}/chapters/{chapter_id}";
    //
    public static final String DELETE_CHAPTERS = "/courses/{course_id}/chapters/{chapter_id}";
    //
    public static final String ADD_LESSONS = "/courses/{course_id}/chapters/{chapter_id}/lessons";
    //
    public static final String UPDATE_LESSONS = "/courses/{course_id}/chapters/{chapter_id}/lessons/{lesson_id}";
    //
    public static final String DELETE_LESSONS = "/courses/{course_id}/chapters/{chapter_id}/lessons/{lesson_id}";
    //
    public static final String GET_USERS = "/users";
    //
    public static final String GET_TEACHERS = "/teachers";
    //
    public static final String GET_COURSES = "/courses";
    //
    public static final String GET_CHAPTERS = "/courses/{course_id}/chapters";
    //
    public static final String GET_LESSONS = "/courses/{course_id}/chapters/{chapter_id}/lessons";
    //
    public static final String LOGIN = "login";
    //
    public static final String USER_INFO = "users/{user_id}";
    //
    public static final String USER_COURSE_INFO = "users/{user_id}/courses/{course_id}/details";
    //
    public static final String USER_COURSE_LESSON_INFO = "users/{user_id}/courses/{course_id}/lessons/{lesson_id}/details";
    //
    public static final String TEACHER_INFO = "teachers/{teacher_id}/details";
    //
    public static final String COURSE_INFO = "courses/{course_id}/details";
    //
    public static final String CHAPTER_INFO = "chapters/{chapter_id}/details";
    //
    public static final String LESSON_INFO = "lessons/{lesson_id}/details";
    //
    public static final String USER_REGISTER = "register";
    //
    public static final String UPDATE_USER = "/users/{user_id}";
    //
    public static final String USER_ENROLL_COURSE = "/users/{user_id}/courses/{course_id}/enroll";
    //
    public static final String USER_RATE_COURSE = "/users/{user_id}/courses/{course_id}/rate";
    //
    public static final String USER_REVIEW_COURSE = "/users/{user_id}/courses/{course_id}/review";
    //
    public static final String USER_VIEW_COURSE_INFO = "courses/{course_id}";
    //
    public static final String USER_SEARCH_REGISTERED_COURSE = "/users/courses/registered";
    //
    public static final String USER_STUDY = "/users/{user_id}/courses/{course_id}/lessons/{lesson_id}/study";
    //
    public static final String ADD_TEACHERS = "/teachers";
}
