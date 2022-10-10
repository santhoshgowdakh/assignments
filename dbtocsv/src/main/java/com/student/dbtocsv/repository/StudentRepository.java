package com.student.dbtocsv.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.student.dbtocsv.entity.Student;

public interface StudentRepository extends JpaRepository<Student, Integer> {

}
