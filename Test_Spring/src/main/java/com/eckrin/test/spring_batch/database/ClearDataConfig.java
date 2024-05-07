package com.eckrin.test.spring_batch.database;

import com.eckrin.test.spring_batch.domain.account.Account;
import com.eckrin.test.spring_batch.domain.account.AccountRepository;
import com.eckrin.test.spring_batch.domain.order.Order;
import com.eckrin.test.spring_batch.domain.order.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.data.builder.RepositoryItemReaderBuilder;
import org.springframework.batch.item.data.builder.RepositoryItemWriterBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.Collections;
import java.util.List;

/**
 * 데이터 삭제 Job Config
 */
@Slf4j
@RequiredArgsConstructor
@Configuration
public class ClearDataConfig {

    private final String JOB_NAME = "clearDataJob";
    private final String STEP_NAME = "clearDataStep";

    private final OrderRepository orderRepository;

    /**
     * 데이터 삭제
     */
    @Bean
    public Job clearDataJob(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new JobBuilder(JOB_NAME, jobRepository)
                .incrementer(new RunIdIncrementer()) // sequential id
                .start(clearDataStep(jobRepository, transactionManager)) // step 설정
                .build();
    }

    /**
     * 데이터 삭제 Step
     */
    @Bean
    @JobScope
    public Step clearDataStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder(STEP_NAME, jobRepository)
                .<Order, Order>chunk(5, transactionManager) // chunkSize: 몇 개 단위로 데이터를 처리할 것인지 지정. 참고로 파라미터로 받아서 사용할 수는 없다.
                .reader(clearDataReader())
                .processor(clearDataProcessor())
                .writer(clearDataWriter())
                .build();
    }

    /**
     * 데이터 삭제
     */
    @Bean
    @StepScope
    public RepositoryItemWriter<Order> clearDataWriter() {
        return new RepositoryItemWriterBuilder<Order>()
                .repository(orderRepository)
                .methodName("delete")
                .build();
    }

    /**
     * 역할 X
     */
    @Bean
    @StepScope
    public ItemProcessor<Order, Order> clearDataProcessor() {
        return new ItemProcessor<Order, Order>() {
            @Override
            public Order process(Order item) throws Exception {
                log.info("item = {}", item.getOrderItem());
                return item;
            }
        };
    }

    /**
     * 데이터 읽어오기
     */
    @Bean
    @StepScope
    public RepositoryItemReader<Order> clearDataReader() {
        return new RepositoryItemReaderBuilder<Order>()
                .name("trOrderReader")
                .repository(orderRepository)
                .methodName("findAll")
                .pageSize(5) // chunkSize와 일치하게 설정
                .arguments(List.of())
                .sorts(Collections.singletonMap("id", Sort.Direction.ASC))
                .build();

    }


}
