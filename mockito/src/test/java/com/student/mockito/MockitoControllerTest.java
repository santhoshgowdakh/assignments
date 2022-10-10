package com.student.mockito;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.hamcrest.Matchers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.student.mockito.entity.Student;
import com.student.mockito.repository.StudentRepository;


@SpringBootTest(classes = MockitoApplication.class)
public class MockitoControllerTest {

	private MockMvc mockMvc;
	
	@Autowired
	private WebApplicationContext context;
	
	@MockBean
	private StudentRepository repo;
	
	ObjectMapper om = new ObjectMapper();
	@BeforeEach
	public void setup() throws Exception {
	    this.mockMvc = MockMvcBuilders.webAppContextSetup(this.context).build();
	}
	@Test
	public void test() {
		assertTrue(true);
	}
	
	@Test
	public void testAddStudent() throws Exception  {
		Student student=new Student(1011, "testadd", "male", "Blr");
		String jsonRequest=om.writeValueAsString(student);
		Mockito.when(repo.save(any(Student.class))).thenReturn(student);
		System.out.println(jsonRequest);
		MvcResult result = mockMvc.perform(post("/addStudent").content(jsonRequest)
				.contentType(MediaType.APPLICATION_JSON_VALUE))
				.andReturn();
		String resultContent = result.getResponse().getContentAsString();
		Student response = om.readValue(resultContent, Student.class);
		assertEquals(1011, response.getStudentId());
	}
	
	@Test
	public void getStudentById() throws Exception  {
		Student student=new Student(1011, "testadd", "male", "Blr");
		Mockito.when(repo.getStudentById(anyInt())).thenReturn(student);
		MvcResult result = mockMvc.perform(get("/id/1002")
				.contentType(MediaType.APPLICATION_JSON_VALUE)).andExpect(status().isOk())
				.andReturn();
		String resultContent = result.getResponse().getContentAsString();
		System.out.println(resultContent);
		Student response = om.readValue(resultContent, Student.class);
		assertEquals(1011, response.getStudentId());
	}
	
	@Test
	public void deleteStudentById() throws Exception  {
		Student student=new Student(1011, "testadd", "male", "Blr");
		Mockito.when(repo.getStudentById(anyInt())).thenReturn(student);
		
			
		MvcResult result = mockMvc.perform(delete("/delete/1011")//.content(jsonRequest)
				.contentType(MediaType.APPLICATION_JSON_VALUE)).andExpect(status().isOk())
				.andReturn();
		String resultContent = result.getResponse().getContentAsString();
		int response = om.readValue(resultContent, Integer.class);
		assertEquals(1011, response);
		verify(repo,times(1)).deleteById(anyInt());
		
	}
	
	
}
