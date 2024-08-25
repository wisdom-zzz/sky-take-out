package com.sky.aspect;

import com.sky.annotation.AutoFill;
import com.sky.constant.AutoFillConstant;
import com.sky.context.BaseContext;
import com.sky.enumeration.OperationType;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.time.LocalDateTime;

@Aspect
@Component
@Slf4j
public class AutoFillAspect {

    /**
     * 切入点
     */
    @Pointcut("execution(* com.sky.mapper.*.*(..)) && @annotation(com.sky.annotation.AutoFill)")
    public void autoFillPointCut() {};


    @Before("autoFillPointCut()")
    public void autoFill(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        AutoFill autoFill = signature.getMethod().getAnnotation(AutoFill.class);
        OperationType operationType = autoFill.value();

        Object[] args = joinPoint.getArgs();
        if (args != null && args.length != 0) {
            return;
        }
        Object arg = args[0];

        LocalDateTime localDateTime = LocalDateTime.now();
        Long currentId = BaseContext.getCurrentId();

        Class<?> aClass = arg.getClass();
        if (operationType == OperationType.INSERT) {
            try {
                Method setCreateTime = aClass.getDeclaredMethod(AutoFillConstant.SET_CREATE_TIME, LocalDateTime.class);
                Method setCreateUser = aClass.getDeclaredMethod(AutoFillConstant.SET_CREATE_USER, long.class);
                Method setUpdateTime = aClass.getDeclaredMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class);
                Method setUpdateUser = aClass.getDeclaredMethod(AutoFillConstant.SET_UPDATE_USER, long.class);
                setCreateTime.invoke(aClass, localDateTime);
                setCreateUser.invoke(aClass, currentId);
                setUpdateTime.invoke(aClass, localDateTime);
                setUpdateUser.invoke(aClass, currentId);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else if (operationType == OperationType.UPDATE) {
            try {
                Method setUpdateTime = aClass.getDeclaredMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class);
                Method setUpdateUser = aClass.getDeclaredMethod(AutoFillConstant.SET_UPDATE_USER, long.class);
                setUpdateTime.invoke(aClass, localDateTime);
                setUpdateUser.invoke(aClass, currentId);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }


}
