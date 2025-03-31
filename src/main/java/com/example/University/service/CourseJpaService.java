package com.example.University.service;

import org.springframework.stereotype.Service;
import com.example.University.repository.*;
import com.example.University.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import java.util.ArrayList;
import java.util.*;

@Service
public class CourseJpaService implements CourseRepository {

    @Autowired
    private CourseJpaRepository courseJpaRepository;

    @Autowired
    private ProfessorJpaRepository professorJpaRepository;

    @Autowired
    private StudentJpaRepository studentJpaRepository;

    @Override
    public ArrayList<Course> getCourses() {
        return new ArrayList<>(courseJpaRepository.findAll());
    }

    @Override
    public Course getCourseById(int courseId) {
        return courseJpaRepository.findById(courseId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Course not found"));
    }

    @Override
    public Course addCourse(Course course) {
        try {
            int professorId = course.getProfessor().getProfessorId();
            Professor professor = professorJpaRepository.findById(professorId)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Professor not found"));

            List<Integer> studentIds = new ArrayList<>();
            for (Student student : course.getStudents()) {
                studentIds.add(student.getStudentId());
            }

            List<Student> students = studentJpaRepository.findAllById(studentIds);
            if (students.size() != studentIds.size()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Some students are not found.");
            }

            course.setProfessor(professor);
            course.setStudents(students);
            return courseJpaRepository.save(course);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error adding course");
        }
    }

    @Override
    public Course updateCourse(int courseId, Course course) {
        Course existingCourse = courseJpaRepository.findById(courseId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Course not found"));

        if (course.getCourseName() != null) {
            existingCourse.setCourseName(course.getCourseName());
        }
        if (course.getCredits() > 0) {
            existingCourse.setCredits(course.getCredits());
        }
        if (course.getProfessor() != null) {
            int professorId = course.getProfessor().getProfessorId();
            Professor professor = professorJpaRepository.findById(professorId)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Professor not found"));
            existingCourse.setProfessor(professor);
        }
        if (course.getStudents() != null) {
            List<Integer> studentIds = new ArrayList<>();
            for (Student student : course.getStudents()) {
                studentIds.add(student.getStudentId());
            }

            List<Student> students = studentJpaRepository.findAllById(studentIds);
            if (students.size() != studentIds.size()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Some students are not found.");
            }
            existingCourse.setStudents(students);
        }
        return courseJpaRepository.save(existingCourse);
    }

    @Override
    public void deleteCourse(int courseId) {
        Course course = courseJpaRepository.findById(courseId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Course not found"));

        List<Student> students = course.getStudents();
        for (Student student : students) {
            student.getCourses().remove(course);
        }
        studentJpaRepository.saveAll(students);

        courseJpaRepository.deleteById(courseId);
    }

    @Override
    public Professor getCourseProfessor(int courseId) {
        Course course = courseJpaRepository.findById(courseId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Course not found"));
        return course.getProfessor();
    }

    @Override
    public List<Student> getCourseStudents(int courseId) {
        Course course = courseJpaRepository.findById(courseId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Course not found"));
        return course.getStudents();
    }
}
