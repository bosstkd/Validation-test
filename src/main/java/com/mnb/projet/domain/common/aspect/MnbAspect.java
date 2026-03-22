package com.mnb.projet.domain.common.aspect;

import com.mnb.projet.domain.common.tools.LocalObjectMapper;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class MnbAspect {

    private static final Logger log = LoggerFactory.getLogger(MnbAspect.class);

    @Around("@annotation(LogExecutionMarker)")
    public Object executeAspect(ProceedingJoinPoint joinPoint) throws Throwable {
        String className = joinPoint.getTarget().getClass().getSimpleName();
        String methodName = joinPoint.getSignature().getName();

        log.info("[{}#{}] - start", className, methodName);
        long start = System.currentTimeMillis();

        try {
            Object result = joinPoint.proceed();
            long elapsed = System.currentTimeMillis() - start;
            log.info("[{}#{}] - end in {}ms | result: {}", className, methodName, elapsed, LocalObjectMapper.convertToString(result));
            return result;
        } catch (Throwable ex) {
            long elapsed = System.currentTimeMillis() - start;
            log.error("[{}#{}] - failed in {}ms | error: {}", className, methodName, elapsed, ex.getMessage());
            throw ex;
        }
    }
}
