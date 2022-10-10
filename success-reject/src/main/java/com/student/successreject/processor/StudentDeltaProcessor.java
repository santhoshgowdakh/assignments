package com.student.successreject.processor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.batch.item.ItemProcessor;

import com.student.successreject.model.Student;


public class StudentDeltaProcessor implements ItemProcessor<Student, Student>{
// in bound and out bound object is Student
	
	private List<String> previousStudents = new ArrayList<>();
	@Override
	public Student process(Student student) throws Exception {
		if(previousStudents.contains(student.toString())) {
            return null;
        }
		System.out.println(previousStudents.contains(student.toString()));
		previousStudents.add(student.toString());
        return student;
	}

}
