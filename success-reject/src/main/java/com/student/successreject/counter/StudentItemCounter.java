package com.student.successreject.counter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ChunkListener;
import org.springframework.batch.core.scope.context.ChunkContext;


public class StudentItemCounter implements ChunkListener{
	private static final Logger log = LoggerFactory.getLogger(StudentItemCounter.class);
	
	@Override
	public void beforeChunk(ChunkContext context) {
		
	}

	@Override
	public void afterChunk(ChunkContext context) {
		int countRead = context.getStepContext().getStepExecution().getReadCount();
        System.out.println(countRead+" records Read from CSV file");
        log.info(countRead+" records Read from CSV file");
        
        int countWrite = context.getStepContext().getStepExecution().getWriteCount();
        System.out.println(countWrite+" records Writting into csv");
        log.error(countWrite+" records Writting into csv");
	}

	@Override
	public void afterChunkError(ChunkContext context) {
	}
}
