package com.student.dbtoxml.config;

import org.springframework.batch.item.ItemProcessor;

import com.student.dbtoxml.entity.Student;

public class StudentProcessor implements ItemProcessor<Student, Student>{
// in bound and out bound object is Student
	@Override
	public Student process(Student student) throws Exception {
		if(student.getStudentId()<1000||student.getStudentId()>10000||student.getName().length()<3)
			return null;
		return student;
	}

}
