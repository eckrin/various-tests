package com.eckrin.test.spring_batch.validateparam;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersInvalidException;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.util.StringUtils;

/**
 * desc: 파일명을 파라미터로 전달, 검증
 * run: --spring.batch.job.name=validatedParamJob -fileName=test.csv
 */
@Slf4j
@RequiredArgsConstructor
@Configuration
public class ValidateParamJobConfig {

    private final String JOB_NAME = "validateParamJob";
    private final String STEP_NAME = "validateParamStep";

    @Value("${test.file-name}")
    private String FILE_NAME;

    /**
     * Job 등록
     */
    @Bean
    public Job validateParamJob(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new JobBuilder(JOB_NAME, jobRepository)
                .incrementer(new RunIdIncrementer()) // sequential id
//                .validator(new FileParamValidator()) // Job에서 validation이 가능하다. -> Spring Batch 5.x 해결방법 못찾음
                .start(validateParamStep(jobRepository, transactionManager)) // step 설정
                .build();
    }

    /**
     * Step 등록
     */
    @JobScope
    @Bean
    public Step validateParamStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder(STEP_NAME, jobRepository)
                .tasklet(validateParamTasklet(), transactionManager) // tasklet 설정
                .build();
    }

    /**
     * Tasklet: Reader-Processor-Writer를 구분하지 않는 단일 step
     */
    @StepScope
    @Bean
    public Tasklet validateParamTasklet() {
        return new Tasklet() {
            @Override
            public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
                log.info("validateParamTasklet");
                log.info("[Tasklet]: fileName = {}", FILE_NAME);

                FileUtils.validateCsv(FILE_NAME);
                return RepeatStatus.FINISHED; // 작업에 대한 Status 명시
            }
        };
    }
}
