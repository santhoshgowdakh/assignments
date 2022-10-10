package com.student.dbtocsv.config;

import java.util.HashMap;
import java.util.Map;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import com.student.dbtocsv.entity.Student;
import com.student.dbtocsv.repository.StudentRepository;


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
	    public FlatFileItemWriter<Student> writer(){
	        FlatFileItemWriter<Student> flatFileItemWriter = new FlatFileItemWriter<>();
	        flatFileItemWriter.setResource(new FileSystemResource("src/main/resources/student-data.csv"));
	        flatFileItemWriter.setLineAggregator(new DelimitedLineAggregator<Student>() {{
	            setDelimiter(",");
	            setFieldExtractor(new BeanWrapperFieldExtractor<Student>() {{
	                setNames(new String[] { "studentId", "name","gender","city"});
	            }});
	        }});
	        flatFileItemWriter.setHeaderCallback(writer -> writer.write("Id,Name,Gender,City"));
	        return flatFileItemWriter;
	    }
	    
	    
	    @Bean
	    public Step step1() {
	        return stepBuilderFactory.get("csv-step").<Student, Student>chunk(10)
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
