package com.eckrin.test.spring_batch.joblistener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;

@Slf4j
public class JobLoggerListener implements JobExecutionListener {

    private static String BEFORE_MESSAGE = "{} Job is Running";

    private static String AFTER_MESSAGE = "{} Job is Done. (Status: {}))";

    /**
     * Job 실행 전 호출
     */
    @Override
    public void beforeJob(JobExecution jobExecution) {
        log.info(BEFORE_MESSAGE, jobExecution.getJobInstance().getJobName());
    }

    /**
     * Job 실행 후 호출
     */
    @Override
    public void afterJob(JobExecution jobExecution) {
        log.info(AFTER_MESSAGE, jobExecution.getJobInstance().getJobName(), jobExecution.getStatus());

        if(jobExecution.getStatus() == BatchStatus.FAILED) {
            // email
            log.info("Job failed");
        }
    }

}
