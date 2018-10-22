package com.cits.batch.batchdemo;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.task.configuration.EnableTask;
import org.springframework.context.annotation.Bean;

@EnableTask
@EnableBatchProcessing
@SpringBootApplication
public class BatchDemoApplication {

	@Autowired
	private JobLauncher jobLauncher;

	@Autowired
	private JobExplorer jobExplorer;

	@Bean
	public CommandLineRunner commandLineRunner() {
		return new BatchDemoCommandLineRunner();
	}


	@Autowired
	public static void main(String[] args) {
		SpringApplication.run(BatchDemoApplication.class, args);
	}

	public static class BatchDemoCommandLineRunner implements CommandLineRunner {

		@Override
		public void run(String... strings) throws Exception {
			System.out.println("Hello, World!");
		}
	}
}
