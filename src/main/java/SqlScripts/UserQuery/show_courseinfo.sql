SELECT
    c.name, c.description,
    COUNT(DISTINCT ch.id) AS num_chapter,
    COUNT(DISTINCT l.id) AS num_lesson,
    COUNT(DISTINCT scl.user_course_user_id) AS num_student
FROM course c
LEFT JOIN chapter ch ON ch.course_id = c.id
LEFT JOIN student_course_lesson scl ON scl.user_course_course_id = c.id
LEFT JOIN lesson l ON l.id = scl.lesson_id
GROUP BY c.id;
