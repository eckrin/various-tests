package com.eckrin.test.spring_batch.file;

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
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.List;

/**
 * 파일 read/write
 */
@Slf4j
@RequiredArgsConstructor
@Configuration
public class FileDataReadWriteJobConfig {

    private final String JOB_NAME = "fileReadWriteJob";
    private final String STEP_NAME = "fileReadWriteStep";

    /**
     * Job 등록
     */
    @Bean
    public Job fileReadWriteJob(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new JobBuilder(JOB_NAME, jobRepository)
                .incrementer(new RunIdIncrementer()) // sequential id
                .start(fileReadWriteStep(jobRepository, transactionManager)) // step 설정
                .build();
    }

    /**
     * Step 등록
     */
    @Bean
    @JobScope
    public Step fileReadWriteStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder(STEP_NAME, jobRepository)
                .<Player, PlayerYear>chunk(5, transactionManager)
                .reader(playerItemReader())
                .processor(playerItemProcessor())
                .writer(playerYearItemWriter())
                .build();
    }

    @Bean
    @StepScope
    public FlatFileItemReader<Player> playerItemReader() {
        return new FlatFileItemReaderBuilder<Player>()
                .name("playerItemReader")
                .resource(new FileSystemResource("players.csv")) // 읽어올 파일
                .lineTokenizer(new DelimitedLineTokenizer()) // 데이터를 어떤 기준으로 자를지 결정 - line 단위
                .fieldSetMapper(new PlayerFieldSetMapper()) // 읽어온 데이터를 객체로 변환
                .linesToSkip(1) // 첫번째 줄 skip
                .build();
    }

    @Bean
    @StepScope
    public ItemProcessor<Player, PlayerYear> playerItemProcessor() {
        return new ItemProcessor<Player, PlayerYear>() {
            @Override
            public PlayerYear process(Player player) throws Exception {
                return new PlayerYear(player);
            }
        };
    }

    @Bean
    @StepScope
    public FlatFileItemWriter<PlayerYear> playerYearItemWriter() {
        BeanWrapperFieldExtractor<PlayerYear> fieldExtractor = new BeanWrapperFieldExtractor<>();
        fieldExtractor.setNames(new String[]{"id", "lastName", "position", "yearsExperience"});
        fieldExtractor.afterPropertiesSet();

        DelimitedLineAggregator<PlayerYear> lineAggregator = new DelimitedLineAggregator<>();
        lineAggregator.setDelimiter(",");
        lineAggregator.setFieldExtractor(fieldExtractor);

        FileSystemResource outputResource = new FileSystemResource("players_output.txt");

        return new FlatFileItemWriterBuilder<PlayerYear>()
                .name("playerItemWriter")
                .resource(outputResource)
                .lineAggregator(lineAggregator)
                .build();
    }


}
