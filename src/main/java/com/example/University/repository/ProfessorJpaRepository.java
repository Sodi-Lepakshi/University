package com.example.University.repository;

import com.example.University.model.Professor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfessorJpaRepository extends JpaRepository<Professor, Integer> {

}

