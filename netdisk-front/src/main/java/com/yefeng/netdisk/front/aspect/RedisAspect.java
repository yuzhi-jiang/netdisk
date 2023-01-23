package com.yefeng.netdisk.front.aspect;

import com.yefeng.netdisk.common.exception.BizException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 *Redis切面处理类
 *
 * @Author: cxx
 * @Date: 2019/1/1 16:30
 */
@Aspect
@Component
public class RedisAspect {
    private Logger logger = LoggerFactory.getLogger(getClass());
    //是否开启redis缓存  true开启   false关闭
    @Value("${mycloud.redis.open: false}")
    private boolean open;

    @Around("execution(* com.yefeng.netdisk.front.util.RedisUtil.*(..))")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        Object result = null;
        if(open){
            try{
                result = point.proceed();
            }catch (Exception e){
                logger.error("redis error", e);
                throw new BizException("Redis服务异常");
            }
        }
        return result;
    }
}
