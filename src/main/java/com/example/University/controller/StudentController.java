
package com.example.University.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;

import com.example.University.model.*;
import com.example.University.service.StudentJpaService;
import java.util.*;

@RestController
public class StudentController {
    @Autowired
    private StudentJpaService studentJpaService;

    @GetMapping("/students")
    public ArrayList<Student> getStudents() {
        return studentJpaService.getStudents();
    }

    @GetMapping("/students/{studentId}")
    public Student getStudentById(@PathVariable("studentId") int studentId) {
        return studentJpaService.getStudentById(studentId);
    }

    @PostMapping("/students")
    public Student addStudent(@RequestBody Student student) {
        return studentJpaService.addStudent(student);
    }

    @PutMapping("/students/{studentId}")
    public Student updateStudent(@PathVariable("studentId") int studentId, @RequestBody Student student) {
        return studentJpaService.updateStudent(studentId, student);
    }

    @DeleteMapping("/students/{studentId}")
    public void deleteStudent(@PathVariable("studentId") int studentId) {
        studentJpaService.deleteStudent(studentId);
    }

    @GetMapping("/students/{studentId}/courses")
    public List<Course> getStudentCourses(@PathVariable("studentId") int studentId) {
        return studentJpaService.getStudentCourses(studentId);
    }
}