package com.sky.aspect;

import com.sky.annotation.AutoFill;
import com.sky.constant.AutoFillConstant;
import com.sky.context.BaseContext;
import com.sky.enumeration.OperationType;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.time.LocalDateTime;

/**
 * 自定义切面，实现公共字段自动填充逻辑
 */
@Aspect
@Component
@Slf4j
public class AutoFillAspect {

    /**
     * 切入点：拦截 mapper 层中被 AutoFill 注解标识的方法
     */
    @Pointcut("execution(* com.sky.mapper.*.*(..)) && @annotation(com.sky.annotation.AutoFill)")//细化切点，只拦截mapper层中被AutoFill注解标识的方法
    public void autoFillPointCut() {
    }

/**
 * 前置通知：在方法执行前执行
 */
    @Before("autoFillPointCut()")
    public void autoFill(JoinPoint joinPoint) {
        log.info("开始进行公共字段自动填充...");

        //获取方法签名
        Method method = ((org.aspectj.lang.reflect.MethodSignature) joinPoint.getSignature()).getMethod();
        //获取方法上的AutoFill注解
        AutoFill autoFill = method.getAnnotation(AutoFill.class);
        //获取数据库注解中的操作类型
        OperationType operationType = autoFill.value();
        //获取方法参数列表（通过连接点获取方法参数列表）
        Object[] args = joinPoint.getArgs();
        //如果方法参数为空，则直接返回(防止空指针异常)
        if (args == null || args.length == 0) {
            return;
        }

        //获取方法参数中的实体对象，获取第一个参数（参数列表中的第一个元素）
        Object entity = args[0];

        
        LocalDateTime now = LocalDateTime.now();
        //获取当前登录用户的id
        Long currentId = BaseContext.getCurrentId();

        try {
            if (operationType == OperationType.INSERT) {
                Method setCreateTime = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_TIME, LocalDateTime.class);
                Method setUpdateTime = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class);
                Method setCreateUser = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_USER, Long.class);
                Method setUpdateUser = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_USER, Long.class);

                //调用实体对象中的setCreateTime方法，设置创建时间为当前时间（通过反射调用实体对象中的方法）
                setCreateTime.invoke(entity, now);
                //调用实体对象中的setUpdateTime方法，设置更新时间为当前时间（通过反射调用实体对象中的方法）
                setUpdateTime.invoke(entity, now);
                //调用实体对象中的setCreateUser方法，设置创建人为当前登录用户（通过反射调用实体对象中的方法）   
                setCreateUser.invoke(entity, currentId);
                //调用实体对象中的setUpdateUser方法，设置更新人为当前登录用户（通过反射调用实体对象中的方法）
                setUpdateUser.invoke(entity, currentId);
            } else if (operationType == OperationType.UPDATE) {
                Method setUpdateTime = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class);
                Method setUpdateUser = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_USER, Long.class);

                setUpdateTime.invoke(entity, now);
                setUpdateUser.invoke(entity, currentId);
            }
        } catch (Exception e) {
            log.error("公共字段自动填充失败：{}", e.getMessage(), e);
        }
    }
}
