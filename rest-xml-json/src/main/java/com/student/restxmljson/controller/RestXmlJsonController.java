package com.student.restxmljson.controller;

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
import com.student.restxmljson.entity.Student;
import com.student.restxmljson.repository.StudentRepository;

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
	@GetMapping(value="/xml",produces =MediaType.APPLICATION_XML_VALUE)
	public ResponseEntity<List<Student>> getAllStudentXML(){
		List<Student> students=studentRepository.findAll();
		return new ResponseEntity<List<Student>>(students, HttpStatus.OK);
		
	}
	@PostMapping("/add")
	public ResponseEntity<Student> addStudent(@RequestBody Student student){
		studentRepository.save(student);
		return new ResponseEntity<Student>(student, HttpStatus.OK);
		
	}
	
	@GetMapping("/id/{studentId}")
	public ResponseEntity<Student> getStudentById(@PathVariable int studentId){
		Student student=studentRepository.getStudentById(studentId);
		return new ResponseEntity<Student>(student, HttpStatus.OK);
		
	}
	@DeleteMapping("/delete/{studentId}")
	public void deleteStudentById(@PathVariable int studentId){
		studentRepository.deleteById(studentId);
		
	}
}
