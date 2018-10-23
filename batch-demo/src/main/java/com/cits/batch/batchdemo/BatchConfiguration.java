package com.cits.batch.batchdemo;


import com.cits.batch.batchdemo.model.Person;
import com.cits.batch.batchdemo.model.mapper.PersonFieldSetMapper;
import com.cits.batch.batchdemo.service.PersonItemProcessor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemStreamReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.ResourceLoader;

import javax.sql.DataSource;

@Configuration
@EnableBatchProcessing
public class BatchConfiguration {
    private final DataSource dataSource;
    private final ResourceLoader resourceLoader;
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Autowired
    public BatchConfiguration(final DataSource dataSource, final JobBuilderFactory jobBuilderFactory,
                              final StepBuilderFactory stepBuilderFactory,
                              final ResourceLoader resourceLoader) {
        this.dataSource = dataSource;
        this.resourceLoader = resourceLoader;
        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;
    }

    @Bean
    @StepScope
    public ItemStreamReader<Person> reader(@Value("#{jobParameters['filePath']}") String filePath) throws Exception {
        return new FlatFileItemReaderBuilder<Person>()
                .name("reader")
                .resource(new ClassPathResource("data.csv"))
//                .resource(resourceLoader.getResource(filePath))
                .delimited()
                .names(new String[]{"firstName", "lastName"})
                .fieldSetMapper(new PersonFieldSetMapper())
                .build();
    }

    @Bean
    public ItemProcessor<Person, Person> processor() {
        return new PersonItemProcessor();
    }

    @Bean
    public ItemWriter<Person> writer() {
        return new JdbcBatchItemWriterBuilder<Person>()
                .beanMapped()
                .dataSource(this.dataSource)
                .sql("INSERT INTO people (first_name, last_name) VALUES (:firstName, :lastName)")
                .build();
    }

    @Bean
    public Job ingestJob() throws Exception {
        return jobBuilderFactory.get("ingestJob")
                .incrementer(new RunIdIncrementer())
                .flow(step1())
                .end()
                .build();
    }

    @Bean
    public Step step1() throws Exception {
        return stepBuilderFactory.get("ingest")
                .<Person, Person>chunk(10)
                .reader(reader(null))
                .processor(processor())
                .writer(writer())
                .build();
    }
}