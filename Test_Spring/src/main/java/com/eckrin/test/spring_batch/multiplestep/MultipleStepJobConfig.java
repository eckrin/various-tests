package com.eckrin.test.spring_batch.multiplestep;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

/**
 * 다중 step을 사용하기 및 step to step 데이터 전달
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
public class MultipleStepJobConfig {

    private final String JOB_NAME = "multipleStepJob";
    private final String STEP_NAME = "multipleStepStep";

    @Bean
    public Job multipleStepJob(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new JobBuilder(JOB_NAME, jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(multipleStep1(jobRepository, transactionManager))
                .next(multipleStep2(jobRepository, transactionManager))
                .next(multipleStep3(jobRepository, transactionManager))
                .build();
    }

    @Bean
    @JobScope
    public Step multipleStep1(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder(STEP_NAME, jobRepository)
                .tasklet((contribution, chunkContext) -> {
                    System.out.println("step1");
                    return RepeatStatus.FINISHED;
                }, transactionManager)
                .build();
    }

    @Bean
    @JobScope
    public Step multipleStep2(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder(STEP_NAME, jobRepository)
                .tasklet((contribution, chunkContext) -> {
                    System.out.println("step2");

                    ExecutionContext executionContext = chunkContext
                            .getStepContext()
                            .getStepExecution()
                            .getJobExecution()
                            .getExecutionContext();
                    executionContext.put("someKey", "hello!!");

                    return RepeatStatus.FINISHED;
                }, transactionManager)
                .build();
    }

    @Bean
    @JobScope
    public Step multipleStep3(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder(STEP_NAME, jobRepository)
                .tasklet((contribution, chunkContext) -> {
                    System.out.println("step3");

                    ExecutionContext executionContext = chunkContext
                            .getStepContext()
                            .getStepExecution()
                            .getJobExecution()
                            .getExecutionContext();

                    System.out.println(executionContext.get("someKey"));

                    return RepeatStatus.FINISHED;
                }, transactionManager)
                .build();
    }
}