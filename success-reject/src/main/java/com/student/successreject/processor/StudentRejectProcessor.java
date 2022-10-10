package com.student.successreject.processor;

import java.util.HashSet;
import java.util.Set;

import org.springframework.batch.item.ItemProcessor;

import com.student.successreject.model.RejectedStudent;
import com.student.successreject.model.Student;

public class StudentRejectProcessor implements ItemProcessor<Student, Student>{
	// in bound and out bound object is Student
	
		@Override
		public Student process(Student student) throws Exception {
			if(student.getPercentage()>=50) {
	            return null;
	        }
			RejectedStudent rejectedStudent=new RejectedStudent();
			rejectedStudent.setStudentId(student.getStudentId());
			rejectedStudent.setName(student.getName());
			rejectedStudent.setPercentage(student.getPercentage());
			rejectedStudent.setRejectionReason(student.getPercentage()+" is less than 50");
	        return rejectedStudent;
		}

	}
