package com.student.dbtoxml.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.student.dbtoxml.entity.Student;

public interface StudentRepository extends JpaRepository<Student, Integer> {

}
