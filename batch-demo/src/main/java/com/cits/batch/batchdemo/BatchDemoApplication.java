package com.cits.batch.batchdemo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.task.configuration.EnableTask;

@EnableTask
@SpringBootApplication
public class BatchDemoApplication {

	@Autowired
	public static void main(String[] args) {
		SpringApplication.run(BatchDemoApplication.class, args);
	}

}
