package ru.otus.job14.batch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@EnableBatchProcessing
@Configuration
public class JobConfig {

    private final JobBuilderFactory jobBuilderFactory;
    private final Step stepGenre;
    private final Step stepAuthor;
    private final Step stepBook;
    private final Step stepReview;

    public JobConfig(JobBuilderFactory jobBuilderFactory, Step stepGenre, Step stepAuthor,
                     Step stepBook, Step stepReview) {
        this.jobBuilderFactory = jobBuilderFactory;
        this.stepGenre = stepGenre;
        this.stepAuthor = stepAuthor;
        this.stepBook = stepBook;
        this.stepReview = stepReview;
    }

    @Bean
    public Job migrationJob() {
        return jobBuilderFactory
                .get("migrationJob")
                .incrementer(new RunIdIncrementer())
                .flow(stepGenre)
                .next(stepAuthor)
                .next(stepBook)
                .next(stepReview)
                .end()
                .build()
                ;
    }

}
