package com.yefeng.netdisk.front.aspect;

import com.google.gson.Gson;
import com.yefeng.netdisk.common.annotation.SysSaveLog;
import com.yefeng.netdisk.common.util.HttpContextUtils;
import com.yefeng.netdisk.common.util.IPUtils;
import com.yefeng.netdisk.front.entity.SysLog;
import com.yefeng.netdisk.front.service.impl.SysLogServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.time.LocalDateTime;

@Slf4j
/**
 * @Author: cxx
 * @Date: 2019/1/1 17:30
 */
@Aspect
@Component
public class SysLogAspect {
    @Autowired
    private SysLogServiceImpl sysLogService;

    @Pointcut("@annotation(com.yefeng.netdisk.common.annotation.SysSaveLog)")
    public void logPointCut() {

    }

    @Around("logPointCut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {


        long beginTime = System.currentTimeMillis();
        //执行方法
        Object result = point.proceed();
        //执行时长(毫秒)
        long time = System.currentTimeMillis() - beginTime;

        //保存日志
        saveSysLog(point, time);

        return result;
    }

    private void saveSysLog(ProceedingJoinPoint joinPoint, long time) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        SysLog sysLog = new SysLog();
        SysSaveLog syslog = method.getAnnotation(SysSaveLog.class);
        if (syslog != null) {
            //注解上的描述
            sysLog.setOperation(syslog.value());
        }

        //请求的方法名
        String className = joinPoint.getTarget().getClass().getName();
        String methodName = signature.getName();
        sysLog.setMethod(className + "." + methodName + "()");

        //请求的参数
        Object[] args = joinPoint.getArgs();
        try {
            String params = new Gson().toJson(args[0]);
            sysLog.setParams(params);
        } catch (Exception e) {

        }

//        RequestAttributes ra = RequestContextHolder.getRequestAttributes();
//        ServletRequestAttributes sra = (ServletRequestAttributes) ra;
//        assert sra != null;
//        HttpServletRequest request = sra.getRequest();




        //获取request
        HttpServletRequest request = HttpContextUtils.getHttpServletRequest();
        //设置IP地址
        sysLog.setIp(IPUtils.getIpAddr(request));
        String subject = request.getHeader("subject");
        String username = request.getHeader("username");

        log.info("subject:{}", subject);
        log.info("username:{}", username);


        //用户名
//        String username = ((SysUserEntity) SecurityUtils.getSubject().getPrincipal()).getUsername();
        sysLog.setUsername(subject+":"+username);

        sysLog.setTime(time);
//        ZoneId zoneId = ZoneId.systemDefault();
//        sysLog.setCreateDate(new Date().toInstant().atZone(zoneId).toLocalDateTime());
        sysLog.setCreateDate(LocalDateTime.now());
        //保存系统日志
        sysLogService.insert(sysLog);


    }
}
