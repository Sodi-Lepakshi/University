package com.example.University.repository;

import com.example.University.model.Course;
import com.example.University.model.Professor;

import java.util.List;

public interface ProfessorRepository {
    List<Professor> getProfessors();

    Professor getProfessorById(int professorId);

    Professor addProfessor(Professor professor);

    Professor updateProfessor(int professorId, Professor professor);

    void deleteProfessor(int professorId);

    List<Course> getProfessorCourses(int professorId);
}
