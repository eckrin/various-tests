package com.eckrin.test.spring_batch;

import com.eckrin.test.spring_batch.config.SpringBatchTestConfig;
import com.eckrin.test.spring_batch.test.BatchJobConfig;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

//@ExtendWith(SpringExtension.class)
@SpringBatchTest
@SpringBootTest(classes = {SpringBatchTestConfig.class, BatchJobConfig.class})
class BatchJobConfigTest {

    @Autowired
    private JobLauncherTestUtils testUtils;

    @Test
    @DisplayName("성공 케이스")
    public void success() throws Exception {
        // when
        JobExecution execution = testUtils.launchJob();

        // then
        Assertions.assertEquals(execution.getExitStatus(), ExitStatus.COMPLETED);
    }
}