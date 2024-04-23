package com.eckrin.test.spring_batch.joblistener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepScope;
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

@Slf4j
@RequiredArgsConstructor
@Configuration
public class ListenerJobConfig {

    private final String JOB_NAME = "jobListenerJob";
    private final String STEP_NAME = "jobListenerStep";

    /**
     * Job 등록
     */
    @Bean
    public Job jobListenerJob(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new JobBuilder(JOB_NAME, jobRepository)
                .incrementer(new RunIdIncrementer()) // sequential id
                .listener(new JobLoggerListener())
                .start(jobListenerStep(jobRepository, transactionManager)) // step 설정
                .build();
    }

    /**
     * Step 등록
     */
    @Bean
    @JobScope
    public Step jobListenerStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder(STEP_NAME, jobRepository)
                .tasklet(jobListenerTasklet(), transactionManager) // tasklet 설정
                .build();
    }

    /**
     * Tasklet: Reader-Processor-Writer를 구분하지 않는 단일 step
     */
    @Bean
    @StepScope
    public Tasklet jobListenerTasklet() {
        return new Tasklet() {
            @Override
            public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
                log.info("JobListener Tasklet Started");
                return RepeatStatus.FINISHED; // 작업에 대한 Status 명시
//                throw new Exception("Failed!!"); // exception 발생 테스트
            }
        };
    }
}
