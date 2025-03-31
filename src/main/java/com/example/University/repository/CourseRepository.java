package com.example.University.repository;

import com.example.University.model.Course;
import com.example.University.model.Professor;
import com.example.University.model.Student;

import java.util.List;

public interface CourseRepository {
    List<Course> getCourses();

    Course getCourseById(int courseId);

    Course addCourse(Course course);

    Course updateCourse(int courseId, Course course);

    void deleteCourse(int courseId);

    Professor getCourseProfessor(int courseId);

    List<Student> getCourseStudents(int courseId);
}
