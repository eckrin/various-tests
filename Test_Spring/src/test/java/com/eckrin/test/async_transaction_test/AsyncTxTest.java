package com.eckrin.test.async_transaction_test;

import com.eckrin.test.async_transaction.FoodRepository;
import com.eckrin.test.async_transaction.FoodService;
import com.eckrin.test.common.CommonTest;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.DefaultTransactionDefinition;

@Slf4j
public class AsyncTxTest extends CommonTest {

    @Autowired
    PlatformTransactionManager transactionManager;
    @Autowired
    private FoodService foodService;
    @Autowired
    private FoodRepository foodRepository;

    @Test
    @DisplayName("비동기 호출 쪽에서 예외 발생해도 트랜잭션 롤백은 발생하지 않음")
    public void txRollbackAsyncTest() {
        try {
            foodService.getFoods();
        } catch(Exception e) {
            log.error("비동기 트랜잭션 에러 처리");
        }

        Assertions.assertThat(foodRepository.count()).isEqualTo(2);
    }

    @Test
    @DisplayName("트랜잭션이 어떻게 할당되는지 확인")
    public void txDefinitionTest() {
        TransactionDefinition defaultTransactionDefinition = new DefaultTransactionDefinition();
        transactionManager.getTransaction(defaultTransactionDefinition);
    }
}
