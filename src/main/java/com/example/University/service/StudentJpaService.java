package com.example.University.service;

import com.example.University.model.Course;
import com.example.University.model.Student;
import com.example.University.repository.CourseJpaRepository;
import com.example.University.repository.StudentJpaRepository;
import com.example.University.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.util.*;

@Service
public class StudentJpaService implements StudentRepository {

    @Autowired
    private StudentJpaRepository studentJpaRepository;

    @Autowired
    private CourseJpaRepository courseJpaRepository;

    @Override
    public ArrayList<Student> getStudents() {
        return new ArrayList<>(studentJpaRepository.findAll());
    }

    @Override
    public Student getStudentById(int studentId) {
        return studentJpaRepository.findById(studentId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Student not found"));
    }

    @Override
    public Student addStudent(Student student) {
        List<Integer> courseIds = new ArrayList<>();
        for (Course course : student.getCourses()) {
            courseIds.add(course.getCourseId());
        }

        List<Course> courses = courseJpaRepository.findAllById(courseIds);

        if (courses.size() != courseIds.size()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Some courses are not found...");
        }

        student.setCourses(courses);

        for (Course course : courses) {
            course.getStudents().add(student);
        }

        Student savedStudent = studentJpaRepository.save(student);
        courseJpaRepository.saveAll(courses);
        return savedStudent;
    }

    @Override
    public Student updateStudent(int studentId, Student student) {
        Student newStudent = studentJpaRepository.findById(studentId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Student not found"));

        if (student.getStudentName() != null) {
            newStudent.setStudentName(student.getStudentName());
        }
        if (student.getEmail() != null) {
            newStudent.setEmail(student.getEmail());
        }
        if (student.getCourses() != null) {
            List<Course> courses = newStudent.getCourses();
            for (Course course : courses) {
                course.getStudents().remove(newStudent);
            }
            courseJpaRepository.saveAll(courses);

            List<Integer> newCourseIds = new ArrayList<>();
            for (Course course : student.getCourses()) {
                newCourseIds.add(course.getCourseId());
            }

            List<Course> newCourses = courseJpaRepository.findAllById(newCourseIds);

            if (newCourses.size() != newCourseIds.size()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Some courses are not found...");
            }

            for (Course course : newCourses) {
                course.getStudents().add(newStudent);
            }
            courseJpaRepository.saveAll(newCourses);
            newStudent.setCourses(newCourses);
        }

        return studentJpaRepository.save(newStudent);
    }

    @Override
    public void deleteStudent(int studentId) {
        Student student = studentJpaRepository.findById(studentId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Student not found"));

        List<Course> courses = student.getCourses();
        for (Course course : courses) {
            course.getStudents().remove(student);
        }
        courseJpaRepository.saveAll(courses);
        studentJpaRepository.deleteById(studentId);
    }

    @Override
    public List<Course> getStudentCourses(int studentId) {
        Student student = studentJpaRepository.findById(studentId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Student not found"));

        return student.getCourses();
    }
}
