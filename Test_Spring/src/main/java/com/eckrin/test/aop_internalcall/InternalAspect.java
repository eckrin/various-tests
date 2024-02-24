package com.eckrin.test.aop_internalcall;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

@Slf4j
@Aspect
public class InternalAspect {

    @Before("execution(* com.eckrin.test.aop_internalcall..*.*(..))")
    public void doLog(JoinPoint joinPoint) {
        log.info("aop 적용됨 : {}", joinPoint.getSignature());
    }
}
