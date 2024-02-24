package com.eckrin.test.aop_internalcall;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class InternalService {

    public void outer() {
        log.info("외부 메서드 호출 로직");
        inner(); // 내부 메서드 호출 (== this.internal();) -> AOP가 적용되지 않음
    }

    public void inner() {
        log.info("내부 메서드 호출 로직");
    }
}
