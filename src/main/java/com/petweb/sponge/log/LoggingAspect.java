package com.petweb.sponge.log;

import com.petweb.sponge.utils.AuthorizationUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class LoggingAspect {

    private final AuthorizationUtil authorizationUtil;

    @Around("@within(com.petweb.sponge.log.Logging)")
    public Object logControllerMethods(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();

        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        String className = methodSignature.getDeclaringType().getSimpleName();
        String methodName = methodSignature.getName();
        String loginId = authorizationUtil.getLoginId() != null ? authorizationUtil.getLoginId().toString() : "비회원";
        String loginType = authorizationUtil.getLoginType() != null ? authorizationUtil.getLoginType() : "비회원";
        log.info("[ID:{}], [TYPE:{}], [REQUEST] {}.{}()", loginId, loginType, className, methodName);
        Object result = joinPoint.proceed();

        long end = System.currentTimeMillis();
        log.info("[ID:{}], [TYPE:{}], [RESPONSE] {}.{}() executed in {}ms", loginId, loginType, className, methodName, end - start);

        return result;
    }
}
