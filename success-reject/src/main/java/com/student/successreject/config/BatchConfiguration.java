package com.student.successreject.config;


import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.MultiResourceItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

import com.student.successreject.counter.StudentItemCounter;
import com.student.successreject.model.Student;
import com.student.successreject.processor.StudentDeltaProcessor;
import com.student.successreject.processor.StudentRejectProcessor;
import com.student.successreject.processor.StudentSuccessProcessor;
import com.student.successreject.util.NoOpWriter;

@Configuration
@EnableBatchProcessing
public class BatchConfiguration {

 @Autowired
 public JobBuilderFactory jobBuilderFactory;
 
 @Autowired
 public StepBuilderFactory stepBuilderFactory;
 
 
 @Bean
 public FlatFileItemReader<Student> reader1(){
  FlatFileItemReader<Student> reader = new FlatFileItemReader<Student>();
  reader.setResource(new FileSystemResource("src/main/resources/input/current.csv")); 
  reader.setLineMapper(lineMapper());
  reader.setLinesToSkip(1);
  return reader;
 }
 @Bean
 public FlatFileItemReader<Student> reader2(){
  FlatFileItemReader<Student> reader = new FlatFileItemReader<Student>();
  reader.setResource(new FileSystemResource("src/main/resources/input/previous.csv")); 
  reader.setLineMapper(lineMapper());
  reader.setLinesToSkip(1);
  return reader;
 }
 
 @Bean
 public FlatFileItemReader<Student> deltaReader(){
  FlatFileItemReader<Student> reader = new FlatFileItemReader<Student>();
  reader.setResource(new FileSystemResource("src/main/resources/delta/delta.csv")); 
  reader.setLineMapper(lineMapper());
  reader.setLinesToSkip(1);
  return reader;
 }
 @Bean
 public FlatFileItemWriter<Student> deltaWriter(){
     FlatFileItemWriter<Student> flatFileItemWriter = new FlatFileItemWriter<>();
     flatFileItemWriter.setResource(new FileSystemResource("src/main/resources/delta/delta.csv"));
     flatFileItemWriter.setLineAggregator(new DelimitedLineAggregator<Student>() {{
         setDelimiter(",");
         setFieldExtractor(new BeanWrapperFieldExtractor<Student>() {{
             setNames(new String[] { "studentId", "name","percentage"});
         }});
     }});
     flatFileItemWriter.setHeaderCallback(writer -> writer.write("Id,Name,Percentage"));
     return flatFileItemWriter;
 }

 @Bean
 public LineMapper<Student> lineMapper() {

     DefaultLineMapper<Student> defaultLineMapper = new DefaultLineMapper<>();
     DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();

     lineTokenizer.setDelimiter(",");

     lineTokenizer.setStrict(false);
     lineTokenizer.setNames(new String[]{"studentId", "name", "percentage" });

     BeanWrapperFieldSetMapper<Student> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
     fieldSetMapper.setTargetType(Student.class);

     defaultLineMapper.setLineTokenizer(lineTokenizer);
     defaultLineMapper.setFieldSetMapper(fieldSetMapper);

     return defaultLineMapper;
 }

 @Bean
 public StudentSuccessProcessor successProcessor(){
  return new StudentSuccessProcessor();
 }
 
 @Bean
 public FlatFileItemWriter<Student> writer1(){
     FlatFileItemWriter<Student> flatFileItemWriter = new FlatFileItemWriter<>();
     flatFileItemWriter.setResource(new FileSystemResource("src/main/resources/output/success-output.csv"));
     flatFileItemWriter.setLineAggregator(new DelimitedLineAggregator<Student>() {{
         setDelimiter(",");
         setFieldExtractor(new BeanWrapperFieldExtractor<Student>() {{
             setNames(new String[] { "studentId", "name","percentage"});
         }});
     }});
     flatFileItemWriter.setHeaderCallback(writer -> writer.write("Id,Name,Percentage"));
     return flatFileItemWriter;
 }
 
 @Bean
 public FlatFileItemWriter<Student> writer2(){
     FlatFileItemWriter<Student> flatFileItemWriter = new FlatFileItemWriter<>();
     flatFileItemWriter.setResource(new FileSystemResource("src/main/resources/output/reject-output.csv"));
     flatFileItemWriter.setLineAggregator(new DelimitedLineAggregator<Student>() {{
         setDelimiter(",");
         setFieldExtractor(new BeanWrapperFieldExtractor<Student>() {{
             setNames(new String[] { "studentId", "name","percentage","rejectionReason"});
         }});
     }});
     flatFileItemWriter.setHeaderCallback(writer -> writer.write("Id,Name,Percentage,Reason"));
     return flatFileItemWriter;
 }
 @Bean
 public NoOpWriter noOpWriter() {
    return new NoOpWriter();
 }

 @Bean
 public StudentDeltaProcessor deltaFileProcessor() {
	return new StudentDeltaProcessor();
}
@Bean
public StudentItemCounter listener() {
   return new StudentItemCounter();
}
@Bean
public Step step1() {
 return stepBuilderFactory.get("step1")
	.<Student, Student> chunk(10)
   .reader(reader2())
   .processor(deltaFileProcessor())
   .writer(noOpWriter())
   .listener(listener())
   .build();
}
@Bean
public Step step2() {
 return stepBuilderFactory.get("step2")
	.<Student, Student> chunk(10)
   .reader(reader1())
   .processor(deltaFileProcessor())
   .writer(deltaWriter())
   .listener(listener())
   .build();
}
 
 @Bean
 public Step step3() {
  return stepBuilderFactory.get("step3")
	.<Student, Student> chunk(10)
    .reader(deltaReader())
    .processor(successProcessor())
    .writer(writer1())
    .listener(listener())
    .build();
 }
 
 @Bean
 public Step step4() {
  return stepBuilderFactory.get("step4")
	.<Student, Student> chunk(10)
    .reader(deltaReader())
    .processor(rejectProcessor())
    .writer(writer2())
    .listener(listener())
    .build();
 }
 
 private StudentRejectProcessor rejectProcessor() {
	return new StudentRejectProcessor();
}

@Bean
 public Job importUserJob() {
  return jobBuilderFactory.get("success-reject")
    .incrementer(new RunIdIncrementer())
    .start(step1()).next(step2()).next(step3()).next(step4())
    .build();
 }
 
}
