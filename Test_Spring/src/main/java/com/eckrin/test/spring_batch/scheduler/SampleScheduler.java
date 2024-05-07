package com.eckrin.test.spring_batch.scheduler;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Collections;

/**
 * 스케줄러를 통해서 Job 실행
 */
@Component
@RequiredArgsConstructor
public class SampleScheduler {

    private final Job clearDataJob;
    private final JobLauncher jobLauncher; // 스케줄링을 활용하여 Job 실행

    @Scheduled(cron = "0 */1 * * * *") // 1분마다 실행할 수 있게 함
    public void testJobRun() throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {

        JobParameters jobParameters = new JobParameters(
                Collections.singletonMap("requestTime", new JobParameter(System.currentTimeMillis(), Long.class))
        );

        jobLauncher.run(clearDataJob, jobParameters);
    }
}
