package com.student.dbtocsv;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.ArgumentMatchers.anyInt;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.junit.After;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.test.AssertFile;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.JobRepositoryTestUtils;
import org.springframework.batch.test.MetaDataInstanceFactory;
import org.springframework.batch.test.StepScopeTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.FileSystemResource;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;

import com.student.dbtocsv.entity.Student;
import com.student.dbtocsv.repository.StudentRepository;

@SpringBatchTest
@SpringBootTest(classes ={ DbtocsvApplication.class } )
class DbtocsvApplicationTests {

	private static final String EXPECTED_OUTPUT = "src/main/resources/expected-data.csv";
	
	private static final String EXPECTED_OUTPUT_1 = "src/main/resources/expected-data-1.csv";

	private static final String TEST_OUTPUT ="src/main/resources/student-data.csv";


	  	@Autowired
	    private JobLauncherTestUtils jobLauncherTestUtils;
	  
	    @Autowired
	    private JobRepositoryTestUtils jobRepositoryTestUtils;
	    
	    @Autowired
	    private RepositoryItemReader<Student> itemReader;
	    
	    @Autowired
	    private FlatFileItemWriter<Student> itemWriter;
	  
	 
	    
	    @After
	    public void cleanUp() {
	        jobRepositoryTestUtils.removeJobExecutions();
	    }

	    
	    private JobParameters defaultJobParameters() {
	       
	    	JobParameters jobParameters = new JobParametersBuilder()
	                .addDate("date", new Date())
	                .addLong("time",System.currentTimeMillis()).toJobParameters();
	    	return jobParameters;
	   }

	    @Test
	    public void testCompletedBatch() throws Exception {
	        FileSystemResource expectedResult = new FileSystemResource(EXPECTED_OUTPUT);
	        FileSystemResource actualResult = new FileSystemResource(TEST_OUTPUT);

	        JobExecution jobExecution = jobLauncherTestUtils.launchJob(defaultJobParameters());
	        JobInstance actualJobInstance = jobExecution.getJobInstance();
	        ExitStatus actualJobExitStatus = jobExecution.getExitStatus();
	      
	       
	        assertEquals(actualJobInstance.getJobName(), "exportStudents");
	        assertEquals(actualJobExitStatus.getExitCode(), "COMPLETED");
	        AssertFile.assertFileEquals(expectedResult, actualResult);
	    }
	    
	    @Test
	    public void launchJob() throws Exception {

	        JobExecution jobExecution = jobLauncherTestUtils.launchJob();
	        assertEquals(jobExecution.getStatus(), BatchStatus.COMPLETED);
	        
	    }
	    
	    @Test
	    public void testReader() throws Exception {
	        StepExecution stepExecution = MetaDataInstanceFactory
	          .createStepExecution(defaultJobParameters());
	        
	        StepScopeTestUtils.doInStepScope(stepExecution, () -> {
	            Student student;
	            itemReader.open(stepExecution.getExecutionContext());
	            while ((student = itemReader.read()) != null) {

	            	assertEquals(true, student instanceof Student);
	              
	            }
	            itemReader.close();
	            return null;
	        });
	    }
	    
	    @Test
	    public void testWriter() throws Exception {
	        FileSystemResource expectedResult = new FileSystemResource(EXPECTED_OUTPUT_1);
	        FileSystemResource actualResult = new FileSystemResource(TEST_OUTPUT);
	        Student student = new Student();
	        student.setStudentId(2001);
	        student.setGender("male");
	        student.setName("WriterTest");
	        student.setCity("MUM");
	        StepExecution stepExecution = MetaDataInstanceFactory
	          .createStepExecution(defaultJobParameters());

	        StepScopeTestUtils.doInStepScope(stepExecution, () -> {
	            itemWriter.open(stepExecution.getExecutionContext());
	            itemWriter.write(Arrays.asList(student));
	            itemWriter.close();
	            return null;
	        });
	        AssertFile.assertFileEquals(expectedResult, actualResult);
	    }
}
