package com.student.xmltodb.config;

import java.util.HashMap;
import java.util.Map;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.xml.StaxEventItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.oxm.xstream.XStreamMarshaller;

import com.student.xmltodb.counter.StudentItemCounter;
import com.student.xmltodb.entity.Student;
import com.student.xmltodb.repository.StudentRepository;

@Configuration
@EnableBatchProcessing
public class BatchConfiguration {

 @Autowired
 public JobBuilderFactory jobBuilderFactory;
 
 @Autowired
 public StepBuilderFactory stepBuilderFactory;
 @Autowired
 private StudentRepository studentRepository;
 
// @Autowired
// public DataSource dataSource;
// 
// @Bean
// public DataSource dataSource() {
//  final DriverManagerDataSource dataSource = new DriverManagerDataSource();
//  dataSource.setDriverClassName("org.postgresql.Driver");
//  dataSource.setUrl("jdbc:postgresql://localhost:5432/student");
//  dataSource.setUsername("postgres");
//  dataSource.setPassword("newpass");
//  
//  return dataSource;
// }
 
// @Bean
// public FlatFileItemReader<Student> reader(){
//  FlatFileItemReader<Student> reader = new FlatFileItemReader<Student>();
//  reader.setResource(new ClassPathResource("student-data.csv")); 
//  reader.setLineMapper(lineMapper());
//  reader.setLinesToSkip(1);
//
////  reader.setLineMapper(new DefaultLineMapper<User>() {{
////   setLineTokenizer(new DelimitedLineTokenizer() {{
////    setNames(new String[] { "name" });
////   }});
////   setFieldSetMapper(new BeanWrapperFieldSetMapper<User>() {{
////    setTargetType(User.class);
////   }});
////   
////  }});
//  
//  return reader;
// }
 
 @Bean
	public StaxEventItemReader<Student> reader(){
		StaxEventItemReader<Student> reader = new StaxEventItemReader<Student>();
		reader.setResource(new ClassPathResource("students.xml"));
		reader.setFragmentRootElementName("student");
		
		Map<String,String> aliasesMap =new HashMap<String,String>();
		aliasesMap.put("student", "com.student.xmltodb.entity.Student");
		XStreamMarshaller marshaller = new XStreamMarshaller();
		marshaller.setAliases(aliasesMap);
		reader.setUnmarshaller(marshaller);
		return reader;
	}
 
// @Bean
// public LineMapper<Student> lineMapper() {
//
//     DefaultLineMapper<Student> defaultLineMapper = new DefaultLineMapper<>();
//     DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
//
//     lineTokenizer.setDelimiter(",");
//
//     lineTokenizer.setStrict(false);
//     lineTokenizer.setNames(new String[]{"studentId", "name", "gender", "city" });
//
//     BeanWrapperFieldSetMapper<Student> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
//     fieldSetMapper.setTargetType(Student.class);
//
//     defaultLineMapper.setLineTokenizer(lineTokenizer);
//     defaultLineMapper.setFieldSetMapper(fieldSetMapper);
//
//     return defaultLineMapper;
// }
 
 @Bean
 public StudentProcessor processor(){
  return new StudentProcessor();
 }
 
// @Bean
// public JdbcBatchItemWriter<Student> writer(){
// //String query= "INSERT INTO student(student_Name,student_stream,subject1_Mark,subject2_Mark,subject3_Mark,student_percentage,student_grade) VALUES (:name,:stream,:subject1Mark,:subject2Mark,:subject3Mark,:percentage,:grade)";
// String query= "INSERT INTO student(id,name,gender,city) VALUES (:studentId,:name,:gender,:city)";
// JdbcBatchItemWriter<Student> writer = new JdbcBatchItemWriter<Student>();
//  writer.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<Student>());
//  writer.setSql(query);
//  writer.setDataSource(dataSource());
//  
//  return writer;
// }
 
 @Bean
 public RepositoryItemWriter<Student> writer() {
	 RepositoryItemWriter<Student> reader = new RepositoryItemWriter<>();
 	reader.setRepository(studentRepository);
 	reader.setMethodName("save");
     return reader;
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
//    .flow(step1())
//    .end()
    .start(step1())
    .build();
 }
 
}
