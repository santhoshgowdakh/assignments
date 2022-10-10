package com.student.dbtoxml.config;

import java.util.HashMap;
import java.util.Map;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.batch.item.xml.StaxEventItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.oxm.xstream.XStreamMarshaller;
import com.student.dbtoxml.entity.Student;
import com.student.dbtoxml.repository.StudentRepository;


@Configuration
@EnableBatchProcessing
public class StudentBatchConfig {
	
		@Autowired
	    private JobBuilderFactory jobBuilderFactory;

		@Autowired
	    private StepBuilderFactory stepBuilderFactory;
	    
		@Autowired
		private StudentRepository studentRepository;
		   

		   
	   
	    @Bean
	    public StudentProcessor processor() {
	        return new StudentProcessor();
	    }
	    
	    
	    @Bean
	    public RepositoryItemReader<Student> reader() {
	    	RepositoryItemReader<Student> reader = new RepositoryItemReader<>();
	    	reader.setRepository(studentRepository);
	    	reader.setMethodName("findAll");
	    	Map<String,Direction> sort=new HashMap<String, Direction>();
	    	sort.put("studentId", Sort.Direction.ASC);
	    	reader.setSort(sort);
	        return reader;
	    }
	    
	    @Bean
	    public StaxEventItemWriter<Student> writer(){
	    	StaxEventItemWriter<Student> itemWriter=new StaxEventItemWriter<Student>();
	    	itemWriter.setResource(new FileSystemResource("src/main/resources/students.xml"));
	    	Map<String,String> aliasesMap =new HashMap<String,String>();
			aliasesMap.put("student", "com.student.dbtoxml.entity.Student");
			XStreamMarshaller marshaller = new XStreamMarshaller();
			marshaller.setAliases(aliasesMap);
			itemWriter.setMarshaller(marshaller);
			itemWriter.setRootTagName("students");
			return itemWriter;
	    	
	    }
	    
	    @Bean
	    public Step step1() {
	        return stepBuilderFactory.get("xml-step").<Student, Student>chunk(10)
	                .reader(reader())
	                .processor(processor())
	                .writer(writer())
	                .build();
	    }

	    @Bean
	    public Job runJob() {
	        return jobBuilderFactory.get("exportStudents")
	        		.incrementer(new RunIdIncrementer())
	                .start(step1()).build();

	    }
	
}
