package com.estudent.restxmljson;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import com.student.restxmljson.RestXmlJsonApplication;
import com.student.restxmljson.controller.RestXmlJsonController;
import com.student.restxmljson.entity.Student;
import com.student.restxmljson.repository.StudentRepository;

//@ExtendWith(SpringExtension.class)
@SpringBootTest (classes = RestXmlJsonApplication.class)
//@AutoConfigureMockMvc
public class JUnitTestCase {

	@Autowired
	private RestXmlJsonController controller;
	
	@Autowired
	private StudentRepository repo;
	
	@Test
	public void test() {
		assertTrue(true);
	}
	@Test
	void testGetAllStudentJson() {
		List<Student> students=controller.getAllStudentJson().getBody();
		assertEquals(true, students.get(0) instanceof Student);
		assertNotNull(students);
		assertEquals(8, students.size());
	}
	@Test
	void testGetAllStudentData() {
		List<Student> students=controller.getAllStudentData().getBody();
		assertEquals(true, students.get(0) instanceof Student);
		assertNotNull(students);
		assertEquals(8, students.size());
		assertEquals(true, students.get(0).getName().startsWith("Mr. ")||students.get(0).getName().startsWith("Ms. "));
	}
	@Test
	void testGetMaleStudents() {
		List<Student> students=controller.getStudentsByGender("Male").getBody();
		assertEquals(true, students.get(0) instanceof Student);
		assertNotNull(students);
		assertEquals(6, students.size());
		 students.forEach(s->assertEquals(true, s.getGender().equalsIgnoreCase("male")));
		 students.forEach(s->assertEquals(false, s.getGender().equalsIgnoreCase("female")));
		 assertInstanceOf(Student.class, students.get(0));
	}
	@Test
	void testGetFemaleStudents() {
		List<Student> students=controller.getStudentsByGender("female").getBody();
		assertEquals(true, students.get(0) instanceof Student);
		assertNotNull(students);
		assertEquals(2, students.size());
		 students.forEach(s->assertEquals(true, s.getGender().equalsIgnoreCase("female")));
		 students.forEach(s->assertEquals(false, s.getGender().equalsIgnoreCase("male")));
	}
	
	@Test
	void getStudentsNameUpperCase() {
		List<String> students=controller.getStudentsNameUpperCase().getBody();
		assertNotNull(students);
		assertEquals(8, students.size());
		students.forEach(s->{for (int i = 0; i < s.length(); i++) {
			assertEquals(true, s.charAt(i)>=65 &&s.charAt(i)<=90);
		}});
		 assertInstanceOf(String.class, students.get(0));
	}
	
	@Test
	public void testGetStudentById(){
		
		assertEquals(1001, controller.getStudentById(1001).getBody().getStudentId());
	}
	
	
	
	
}
