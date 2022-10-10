package com.student.successreject.util;

import java.util.List;

import org.springframework.batch.item.ItemWriter;

public class NoOpWriter  implements ItemWriter {
		  
		@Override
		public void write(List items) throws Exception {
		
			
		}
		
}
