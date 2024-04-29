package com.eckrin.test.spring_batch;

import com.eckrin.test.config.DatasourceConfig;
import com.eckrin.test.spring_batch.config.SpringBatchTestConfig;
import com.eckrin.test.spring_batch.database.TrMigrationConfig;
import com.eckrin.test.spring_batch.domain.account.AccountRepository;
import com.eckrin.test.spring_batch.domain.order.Order;
import com.eckrin.test.spring_batch.domain.order.OrderRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;

//@ExtendWith(SpringExtension.class) // 테스트 코드에서는 스프링이 아니라 JUnit이 빈 주입을 담당하는데, 동적으로 Paramter Resolver를 통해 주입 가능
@SpringBatchTest
@SpringBootTest(classes = {SpringBatchTestConfig.class, TrMigrationConfig.class, DatasourceConfig.class})
class TrMigrationConfigTest {

    @Autowired
    private JobLauncherTestUtils testUtils;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private AccountRepository accountRepository;

//    @Autowired
//    private FoodService foodService; -> 얘는 DI가 안되는데, 왜그러는지 모르겠음.

    @AfterEach
    public void cleanUp() {
        orderRepository.deleteAllInBatch();
        accountRepository.deleteAllInBatch();
    }

    @Test
    @DisplayName("성공 케이스 - Order 테이블에 데이터가 없는 상태 테스트")
    public void success_noData() throws Exception {
        // when
        JobExecution execution = testUtils.launchJob();

        // then
        Assertions.assertEquals(execution.getExitStatus(), ExitStatus.COMPLETED);
        Assertions.assertEquals(0, accountRepository.count());
    }

    @Test
    @DisplayName("성공 케이스 - Order 테이블에 데이터가 존재하는 상태 테스트")

    public void success_DataExist() throws Exception {
        // given
        Order order1 = new Order(null, "kakao_gift", 15000L, new Date());
        Order order2 = new Order(null, "naver_gift", 15000L, new Date());

        orderRepository.save(order1);
        orderRepository.save(order2);

        // when
        JobExecution execution = testUtils.launchJob(); // job 실행

        // then
        Assertions.assertEquals(execution.getExitStatus(), ExitStatus.COMPLETED);
        Assertions.assertEquals(2, accountRepository.count());
    }

}