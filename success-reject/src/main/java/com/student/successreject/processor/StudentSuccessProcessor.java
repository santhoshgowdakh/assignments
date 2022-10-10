package com.student.successreject.processor;

import java.util.HashSet;
import java.util.Set;

import org.springframework.batch.item.ItemProcessor;

import com.student.successreject.model.Student;


public class StudentSuccessProcessor implements ItemProcessor<Student, Student>{
// in bound and out bound object is Student
	
	@Override
	public Student process(Student student) throws Exception {
		if(student.getPercentage()<50) {
            return null;
        }
		
        return student;
	}

}
