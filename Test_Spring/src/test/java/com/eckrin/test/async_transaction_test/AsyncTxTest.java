package com.eckrin.test.async_transaction_test;

import com.eckrin.test.async_transaction.FoodRepository;
import com.eckrin.test.async_transaction.FoodService;
import com.eckrin.test.common.CommonTest;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
public class AsyncTxTest extends CommonTest {

    @Autowired
    private FoodService foodService;
    @Autowired
    private FoodRepository foodRepository;

    @Test
    @DisplayName("비동기 호출 쪽에서 예외 발생해도 트랜잭션 롤백은 발생하지 않음")
    public void txRollbackASyncTest() {
        try {
            foodService.getFoods();
        } catch(Exception e) {
            log.error("비동기 트랜잭션 에러 처리");
        }

        Assertions.assertThat(foodRepository.count()).isEqualTo(2);
    }
}
