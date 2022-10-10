package com.student.mockito.controller;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.MediaType;
import com.student.mockito.entity.Student;
import com.student.mockito.repository.StudentRepository;

import javassist.NotFoundException;

@RestController
public class RestXmlJsonController {

	@Autowired
	private StudentRepository studentRepository;
	
	// for json response
	@GetMapping("/json")
	public ResponseEntity<List<Student>> getAllStudentJson(){
		List<Student> students=studentRepository.findAll();
		return new ResponseEntity<List<Student>>(students, HttpStatus.OK);
		
	}
	
	//stream - forEach()
	@GetMapping("/q1/foreach")
	public ResponseEntity<List<Student>> getAllStudentData(){
		List<Student> students=studentRepository.findAll();
		students.stream()
				.forEach(s->{
					 if(s.getGender().equals("male")||s.getGender().equals("Male")) 
						s.setName("Mr. "+s.getName());
					 else
						 s.setName("Ms. "+s.getName());
						
				});
				
		return new ResponseEntity<List<Student>>(students, HttpStatus.OK);
		
	}
	
	//filter by gender
	@GetMapping("/q1/filter/{gender}")
	public ResponseEntity<List<Student>> getStudentsByGender(@PathVariable String gender){
		List<Student> students=studentRepository.findAll().stream()
				.filter(s->s.getGender().equalsIgnoreCase(gender))
				.collect(Collectors.toList());
				
		return new ResponseEntity<List<Student>>(students, HttpStatus.OK);
		
	}
	//map
	@GetMapping("/q1/map")
	public ResponseEntity<List<String>> getStudentsNameUpperCase(){
		List<String> studentNames=studentRepository.findAll().stream().map(s->s.getName().toUpperCase())
		        .collect(Collectors.toList());
		Collections.sort(studentNames);
		return new ResponseEntity<List<String>>(studentNames, HttpStatus.OK);
		
	}
	
	// for XML response
//	@GetMapping(value="/xml",produces =MediaType.APPLICATION_XML_VALUE)
//	public ResponseEntity<List<Student>> getAllStudentXML(){
//		List<Student> students=studentRepository.findAll();
//		return new ResponseEntity<List<Student>>(students, HttpStatus.OK);
//		
//	}
	@PostMapping("/addStudent")
	public Student addStudent(@RequestBody Student student){
		studentRepository.save(student);
		return student;
		
	}
	
	@GetMapping("/id/{studentId}")
	public Student getStudentById(@PathVariable int studentId){
		System.out.println(studentId);
		Student student=studentRepository.getStudentById(studentId);
		return student;
		
	}
	@DeleteMapping("/delete/{studentId}")
	public int deleteStudentById(@PathVariable int studentId) throws NotFoundException{
		Student student=studentRepository.getStudentById(studentId);
		if(student==null)
			throw new NotFoundException("Not Found");
		else
		studentRepository.deleteById(studentId);
		return studentId;
		
	}
}
