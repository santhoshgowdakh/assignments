package com.student.xmltodb.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.student.xmltodb.entity.Student;

public interface StudentRepository extends JpaRepository<Student, Integer> {

}
