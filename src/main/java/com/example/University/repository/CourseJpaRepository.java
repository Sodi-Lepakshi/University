package com.example.University.repository;

import com.example.University.model.Course;
import com.example.University.model.Professor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public interface CourseJpaRepository extends JpaRepository<Course, Integer> {
    ArrayList<Course> findByProfessor(Professor professor);
}

