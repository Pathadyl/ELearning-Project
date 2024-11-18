DROP SCHEMA IF EXISTS elearning;

CREATE SCHEMA elearning;
USE
    elearning;

-- Table for Admin Users
CREATE TABLE admin
(
    id                   BIGINT PRIMARY KEY AUTO_INCREMENT,
    username             VARCHAR(255)                        NULL,
    password             VARCHAR(255)                        NULL,
    created_date         TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_date         TIMESTAMP                           NULL,
    status               VARCHAR(50)                         NOT NULL
);

-- Table for Users
CREATE TABLE user
(
    id                   BIGINT PRIMARY KEY AUTO_INCREMENT,
    username             VARCHAR(255)                        NULL,
    password             VARCHAR(255)                        NULL,
    created_date         TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_date         TIMESTAMP                           NULL,
    status               VARCHAR(50)                         NOT NULL,
    nickname             VARCHAR(50)                         NULL
);

-- Table for FullName
CREATE TABLE fullname
(
    id                   BIGINT PRIMARY KEY AUTO_INCREMENT,
    first_name           VARCHAR(255)                        NULL,
    mid_name             VARCHAR(255)                        NULL,
    last_name            VARCHAR(255)                        NULL,
    status               VARCHAR(50)                         NOT NULL,
    created_date         TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_date         TIMESTAMP                           NULL,
    user_id              BIGINT,
    FOREIGN KEY (user_id) REFERENCES user (id)
);

-- Table for Address
CREATE TABLE address
(
    id                   BIGINT PRIMARY KEY AUTO_INCREMENT,
    country              VARCHAR(255)                     NULL,
    city                 VARCHAR(255)                        NULL,
    district             VARCHAR(255)                        NULL,
    street               VARCHAR(255)                        NULL,
    address_detail       VARCHAR(255)                        NULL
);

-- Relationship table for user-address
CREATE TABLE user_address
(
    user_id              BIGINT,
    address_id           BIGINT,
    PRIMARY KEY (user_id, address_id),
    FOREIGN KEY (user_id) REFERENCES user (id),
    FOREIGN KEY (address_id) REFERENCES address (id)
);

-- Table for Teachers
CREATE TABLE teacher
(
    id                   BIGINT PRIMARY KEY AUTO_INCREMENT,
    name                 VARCHAR(255)                        NOT NULL,
    username             VARCHAR(255)                        NULL,
    password             VARCHAR(255)                        NULL,
    created_date         TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_date         TIMESTAMP                           NULL,
    status               VARCHAR(50)                         NOT NULL
);

-- Table for Courses
CREATE TABLE course
(
    id                   BIGINT PRIMARY KEY AUTO_INCREMENT,
    name                 VARCHAR(255)                        NOT NULL,
    description          VARCHAR(1000)                       NULL,
    status               VARCHAR(50)                         NOT NULL,
    created_date         TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_date         TIMESTAMP                           NULL,
    teacher_id           BIGINT,
    FOREIGN KEY (teacher_id) REFERENCES teacher (id)
);

-- Table for Chapters
CREATE TABLE chapter
(
    id                   BIGINT PRIMARY KEY AUTO_INCREMENT,
    name                 VARCHAR(255)                        NOT NULL,
    description          VARCHAR(1000)                       NULL,
    status               VARCHAR(50)                         NOT NULL,
    `order`              INT                                 NOT NULL,
    created_date         TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_date         TIMESTAMP                           NULL,
    course_id            BIGINT,
    FOREIGN KEY (course_id) REFERENCES course (id)
);

-- Table for Lessons
CREATE TABLE lesson
(
    id                   BIGINT PRIMARY KEY AUTO_INCREMENT,
    name                 VARCHAR(255)                        NOT NULL,
    description          VARCHAR(1000)                       NULL,
    status               VARCHAR(50)                         NOT NULL,
    type                 VARCHAR(50)                         NOT NULL,
    url                  VARCHAR(50) NULL,
    `order`              INT                                 NOT NULL,
    created_date         TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_date         TIMESTAMP NULL,
    chapter_id           BIGINT,
    FOREIGN KEY (chapter_id) REFERENCES chapter (id)
);

-- Relationship Table for User-Course
CREATE TABLE user_course
(
    user_id              BIGINT,
    course_id            BIGINT,
    rating               INT NULL,
    review               VARCHAR(5000)                       NULL,
    status               VARCHAR(50)                         NOT NULL,
    created_date         TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_date         TIMESTAMP                           NULL,
    PRIMARY KEY (user_id, course_id),
    FOREIGN KEY (user_id) REFERENCES user (id),
    FOREIGN KEY (course_id) REFERENCES course (id)
);

-- Relationship Table for User-Course-Lesson
CREATE TABLE user_course_lesson
(
    user_id              BIGINT,
    course_id            BIGINT,
    lesson_id            BIGINT,
    status               VARCHAR(50)                         NOT NULL,
    created_date         TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_date         TIMESTAMP NULL,
    PRIMARY KEY (user_id, course_id, lesson_id),
    FOREIGN KEY (user_id, course_id) REFERENCES user_course (user_id, course_id),
    FOREIGN KEY (lesson_id) REFERENCES lesson (id)
);