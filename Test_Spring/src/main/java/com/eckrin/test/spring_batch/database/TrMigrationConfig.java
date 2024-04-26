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
 * 주문(order) 테이블 -> 정산(accounts) 테이블 데이터 이관 가정
 */
@Slf4j
@RequiredArgsConstructor
@Configuration
public class TrMigrationConfig {

    private final String JOB_NAME = "trMigrationJob";
    private final String STEP_NAME = "trMigrationStep";

    private final OrderRepository orderRepository;
    private final AccountRepository accountRepository;

    /**
     * Job 등록
     * Chunk: ItemReader를 통해서 읽은 아이템을 쌓아두는 단위
     */
    @Bean
    public Job trMigrationJob(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new JobBuilder(JOB_NAME, jobRepository)
                .incrementer(new RunIdIncrementer()) // sequential id
                .start(trMigrationStep(jobRepository, transactionManager)) // step 설정
                .build();
    }

    /**
     * Step 등록
     * 참고로 chunk란
     */
    @Bean
    @JobScope
    public Step trMigrationStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder(STEP_NAME, jobRepository)
                .<Order, Account>chunk(5, transactionManager) // chunkSize: 몇 개 단위로 데이터를 처리할 것인지 지정. 참고로 파라미터로 받아서 사용할 수는 없다.
                .reader(trOrderReader())
                .processor(trOrderProcessor())
                .writer(trOrderWriter())
                .build();
    }

    /**
     * Account를 Repository의 method를 명시하여 데이터 처리
     */
    @Bean
    @StepScope
    public RepositoryItemWriter<Account> trOrderWriter() {
        return new RepositoryItemWriterBuilder<Account>()
                .repository(accountRepository)
                .methodName("save")
                .build();
    }

    /**
     * Order를 Account로 변경하는 Processor
     */
    @Bean
    @StepScope
    public ItemProcessor<Order, Account> trOrderProcessor() {
        return new ItemProcessor<Order, Account>() {
            @Override
            public Account process(Order item) throws Exception {
                return new Account(item);
            }
        };
    }

    @Bean
    @StepScope
    public RepositoryItemReader<Order> trOrderReader() {
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
