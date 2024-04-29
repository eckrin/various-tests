package com.eckrin.test.spring_batch.conditionalstep;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

/**
 * step 결과의 따른 다음 step 분기 처리
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
public class ConditionalStepJobConfig {

    private final String JOB_NAME = "conditionalStepJob";
    private final String STEP_NAME = "conditionalStepStep";

    @Bean
    public Job conditionalStepJob(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new JobBuilder(JOB_NAME, jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(conditionalStartStep(jobRepository, transactionManager))
                .on("FAILED").to(conditionalFailStep(jobRepository, transactionManager))
                .from(conditionalStartStep(jobRepository, transactionManager))
                .on("COMPLETED").to(conditionalCompletedStep(jobRepository, transactionManager))
                .from(conditionalStartStep(jobRepository, transactionManager))
                .on("*").to(conditionalAllStep(jobRepository, transactionManager))
                .end()
                .build();
    }

    @Bean
    @JobScope
    public Step conditionalStartStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder(STEP_NAME, jobRepository)
                .tasklet(new Tasklet() {
                    @Override
                    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
                        System.out.println("conditional Start Step");
                        return RepeatStatus.FINISHED;
//                        throw new Exception("Exception!!");
                    }
                }, transactionManager)
                .build();
    }

    @Bean
    @JobScope
    public Step conditionalAllStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder(STEP_NAME, jobRepository)
                .tasklet(new Tasklet() {
                    @Override
                    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
                        System.out.println("conditional All Step");
                        return RepeatStatus.FINISHED;
                    }
                }, transactionManager)
                .build();
    }

    @Bean
    @JobScope
    public Step conditionalFailStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder(STEP_NAME, jobRepository)
                .tasklet(new Tasklet() {
                    @Override
                    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
                        System.out.println("conditional Fail Step");
                        return RepeatStatus.FINISHED;
                    }
                }, transactionManager)
                .build();
    }

    @Bean
    @JobScope
    public Step conditionalCompletedStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder(STEP_NAME, jobRepository)
                .tasklet(new Tasklet() {
                    @Override
                    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
                        System.out.println("conditional Completed Step");
                        return RepeatStatus.FINISHED;
                    }
                }, transactionManager)
                .build();
    }
}