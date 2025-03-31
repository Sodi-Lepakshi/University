package com.example.University.repository;

import com.example.University.model.Course;
import com.example.University.model.Student;

import java.util.List;

public interface StudentRepository {
    List<Student> getStudents();

    Student getStudentById(int studentId);

    Student addStudent(Student student);

    Student updateStudent(int studentId, Student student);

    void deleteStudent(int studentId);

    List<Course> getStudentCourses(int studentId);
}
