package com.eckrin.test.aop_internalcall;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@Slf4j
@Import(InternalAspect.class)
@SpringBootTest
class InternalAOPTest {

    @Autowired
    InternalService service; // 프록시 적용된 DI

    @Test
    void external() {
        service.outer();
    }

    @Test
    void internal() {
        service.inner();
    }

}