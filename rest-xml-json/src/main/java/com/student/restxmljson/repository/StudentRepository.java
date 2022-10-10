package com.student.restxmljson.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.student.restxmljson.entity.Student;

public interface StudentRepository extends JpaRepository<Student, Integer> {

	@Query("select student from Student student where student.studentId=?1")
	Student getStudentById(int studentId);

}
