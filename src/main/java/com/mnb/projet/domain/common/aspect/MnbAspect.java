package com.mnb.projet.domain.common.aspect;

import com.mnb.projet.domain.common.tools.LocalObjectMapper;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class MnbAspect {

    @Around("@annotation(LogExecutionMarker)")
    public Object excuteAspect(ProceedingJoinPoint joinPoint) throws Throwable {
        Object proceed = joinPoint.proceed();
        System.out.println("SendEvent: " + LocalObjectMapper.convertToString(proceed));
        return proceed;
    }
}
