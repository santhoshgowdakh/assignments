package com.student.restxmljson;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.student.restxmljson.controller.RestXmlJsonController;
import com.student.restxmljson.repository.StudentRepository;


@SpringBootApplication
public class RestXmlJsonApplication {

	public static void main(String[] args) {
		SpringApplication.run(RestXmlJsonApplication.class, args);
	}

	
	
	
//	@Bean
//	public StudentRepository getRepository() {
//		return new SimpleJp
//	}
}
