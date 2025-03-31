package com.example.University.service;

import com.example.University.model.Course;
import com.example.University.model.Professor;
import com.example.University.repository.ProfessorRepository;
import com.example.University.repository.CourseJpaRepository;
import com.example.University.repository.ProfessorJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.util.*;

@Service
public class ProfessorJpaService implements ProfessorRepository {

    @Autowired
    private ProfessorJpaRepository professorJpaRepository;

    @Autowired
    private CourseJpaRepository courseJpaRepository;

    @Override
    public ArrayList<Professor> getProfessors() {
        return new ArrayList<>(professorJpaRepository.findAll());
    }

    @Override
    public Professor getProfessorById(int professorId) {
        return professorJpaRepository.findById(professorId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Professor not found"));
    }

    @Override
    public Professor addProfessor(Professor professor) {
        return professorJpaRepository.save(professor);
    }

    @Override
    public Professor updateProfessor(int professorId, Professor professor) {
        Professor newProfessor = professorJpaRepository.findById(professorId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Professor not found"));

        if (professor.getProfessorName() != null) {
            newProfessor.setProfessorName(professor.getProfessorName());
        }
        if (professor.getDepartment() != null) {
            newProfessor.setDepartment(professor.getDepartment());
        }

        return professorJpaRepository.save(newProfessor);
    }

    @Override
    public void deleteProfessor(int professorId) {
        Professor professor = professorJpaRepository.findById(professorId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Professor not found"));

        List<Course> courses = courseJpaRepository.findByProfessor(professor);

        for (Course course : courses) {
            course.setProfessor(null);
        }
        courseJpaRepository.saveAll(courses);
        professorJpaRepository.deleteById(professorId);
    }

    @Override
    public List<Course> getProfessorCourses(int professorId) {
        Professor professor = professorJpaRepository.findById(professorId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Professor not found"));

        return courseJpaRepository.findByProfessor(professor);
    }
}
