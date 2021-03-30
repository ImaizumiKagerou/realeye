package com.realeye.backend.utils.annotation;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class StringTrimAspect {

    @Around(value = "@annotation(com.realeye.backend.utils.annotation.StringTrim) && execution(* com.realeye.*..*.*(..))")
    public Object aroundAspect(ProceedingJoinPoint jp) throws Throwable {
        Object[] args = jp.getArgs(); // 参数值

        for (int i = 0; i < args.length; i++) {
            if (args[i] != null && args[i].getClass().equals(String.class)) {
                args[i] = args[i].toString().trim();
            }
        }

        return jp.proceed(args);
    }

}
