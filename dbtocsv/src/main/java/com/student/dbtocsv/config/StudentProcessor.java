package com.student.dbtocsv.config;

import org.springframework.batch.item.ItemProcessor;

import com.student.dbtocsv.entity.Student;

public class StudentProcessor implements ItemProcessor<Student, Student>{
// in bound and out bound object is Student
	@Override
	public Student process(Student student) throws Exception {
		// validations
		if(student.getStudentId()<1000||student.getStudentId()>10000||student.getName().length()<3)
			return null;
		return student;
	}

}
