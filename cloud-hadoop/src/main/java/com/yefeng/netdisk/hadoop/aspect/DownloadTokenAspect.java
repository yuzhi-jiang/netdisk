package com.yefeng.netdisk.hadoop.aspect;

import com.yefeng.netdisk.common.util.FileNameUtil;
import com.yefeng.netdisk.common.util.JWTUtil;
import com.yefeng.netdisk.common.validator.Assert;
import com.yefeng.netdisk.hadoop.config.DownloadTokenConfig;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * This class is for
 *
 * @author 夜枫
 * @version 2023-02-18 21:48
 */
@Aspect
@Component
public class DownloadTokenAspect {

    @Pointcut("@annotation(com.yefeng.netdisk.hadoop.annotation.DownloadToken)")
    public void PointCut() {

    }

    @Resource
    private DownloadTokenConfig downloadTokenConfig;

    @Around("PointCut()")
    public Object validateToken(ProceedingJoinPoint joinPoint) throws Throwable {
        Object result = null;
        System.out.println(downloadTokenConfig.getTokenPosition());
        System.out.println(downloadTokenConfig.getEnableValidateToken());
        if (!downloadTokenConfig.getEnableValidateToken()) {
            result = joinPoint.proceed();
            return result;
        }
        Map fieldsName = getFieldsName(joinPoint);
        String path = fieldsName.get("path").toString();


        String downloadToken = "";
        if ("header".equals(downloadTokenConfig.getTokenPosition())) {
            RequestAttributes ra = RequestContextHolder.getRequestAttributes();
            ServletRequestAttributes sra = (ServletRequestAttributes) ra;
            HttpServletRequest request = sra.getRequest();
            downloadToken = request.getHeader("downloadToken");
        } else {
            downloadToken = (String) fieldsName.get("downloadToken");
        }


        Assert.isBlank(downloadToken, "downloadToken is null");

            JWTUtil.validateToken(downloadToken);

        String fileName = FileNameUtil.getFileNameByPath(path);

        Object[] payload = JWTUtil.getPayloadFromToken(downloadToken, "file_name", "type");
        if (payload.length == 2 && "download".equals(payload[1]) && payload[0].equals(fileName)) {
            //校验通过可以下载
            result = joinPoint.proceed();

        } else {
            throw new RuntimeException("token 校验失败");
        }
        return result;
    }


    private static Map getFieldsName(ProceedingJoinPoint joinPoint) throws ClassNotFoundException, NoSuchMethodException {
        String classType = joinPoint.getTarget().getClass().getName();
        String methodName = joinPoint.getSignature().getName();
        // 参数值
        Object[] args = joinPoint.getArgs();
        Class<?>[] classes = new Class[args.length];
        for (int k = 0; k < args.length; k++) {
            if (!args[k].getClass().isPrimitive()) {
                // 获取的是封装类型而不是基础类型
                String result = args[k].getClass().getName();
                Class s = map.get(result);
                classes[k] = s == null ? args[k].getClass() : s;
            }
        }
        ParameterNameDiscoverer pnd = new DefaultParameterNameDiscoverer();
        // 获取指定的方法，第二个参数可以不传，但是为了防止有重载的现象，还是需要传入参数的类型
        Method method = Class.forName(classType).getMethod(methodName, classes);
        // 参数名
        String[] parameterNames = pnd.getParameterNames(method);
        // 通过map封装参数和参数值
        HashMap<String, Object> paramMap = new HashMap();
        for (int i = 0; i < parameterNames.length; i++) {
            paramMap.put(parameterNames[i], args[i]);
        }
        return paramMap;
    }

    private static HashMap<String, Class> map = new HashMap<String, Class>() {
    };
}
