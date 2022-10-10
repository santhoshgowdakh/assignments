package com.student.studentdata.config;

import javax.sql.DataSource;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import com.student.studentdata.counter.StudentItemCounter;
import com.student.studentdata.entity.Student;

@Configuration
@EnableBatchProcessing
public class BatchConfiguration {

 @Autowired
 public JobBuilderFactory jobBuilderFactory;
 
 @Autowired
 public StepBuilderFactory stepBuilderFactory;
 

 @Bean
 public DataSource dataSource() {
  final DriverManagerDataSource dataSource = new DriverManagerDataSource();
  dataSource.setDriverClassName("org.postgresql.Driver");
  dataSource.setUrl("jdbc:postgresql://localhost:5432/student");
  dataSource.setUsername("postgres");
  dataSource.setPassword("newpass");
  
  return dataSource;
 }
 
 @Bean
 public FlatFileItemReader<Student> reader(){
  FlatFileItemReader<Student> reader = new FlatFileItemReader<Student>();
  reader.setResource(new ClassPathResource("student-data.csv")); 
  reader.setLineMapper(lineMapper());
  reader.setLinesToSkip(1); 
  return reader;
 }
 
 @Bean
 public LineMapper<Student> lineMapper() {

     DefaultLineMapper<Student> defaultLineMapper = new DefaultLineMapper<>();
     DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();

     lineTokenizer.setDelimiter(",");

     lineTokenizer.setStrict(false);
     lineTokenizer.setNames(new String[]{"studentId", "name", "gender", "city" });

     BeanWrapperFieldSetMapper<Student> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
     fieldSetMapper.setTargetType(Student.class);

     defaultLineMapper.setLineTokenizer(lineTokenizer);
     defaultLineMapper.setFieldSetMapper(fieldSetMapper);

     return defaultLineMapper;
 }
 
 @Bean
 public StudentProcessor processor(){
  return new StudentProcessor();
 }
 
 @Bean
 public JdbcBatchItemWriter<Student> writer(){
String query= "INSERT INTO student(id,name,gender,city) VALUES (:studentId,:name,:gender,:city)";
 JdbcBatchItemWriter<Student> writer = new JdbcBatchItemWriter<Student>();
  writer.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<Student>());
  writer.setSql(query);
  writer.setDataSource(dataSource());
  
  return writer;
 }

@Bean
public StudentItemCounter listener() {
   return new StudentItemCounter();
}

 
 @Bean
 public Step step1() {
  return stepBuilderFactory.get("step1")
	.<Student, Student> chunk(10)
    .reader(reader())
    .processor(processor())
    .writer(writer())
    .listener(listener())
    .build();
 }
 
 @Bean
 public Job importUserJob() {
  return jobBuilderFactory.get("importStudentJob")
    .incrementer(new RunIdIncrementer())
    .start(step1())
    .build();
 }
 
}
