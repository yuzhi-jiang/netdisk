package com.yefeng.netdisk.front.aspect;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.gson.reflect.TypeToken;
import com.yefeng.netdisk.common.result.ApiResult;
import com.yefeng.netdisk.common.result.ResultUtil;
import com.yefeng.netdisk.front.vo.ListDataVo;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;

@Component
@Aspect
public class PageInterceptor {

    @Around("@annotation(com.yefeng.netdisk.front.annotation.Page)")
    public <T> Object page(ProceedingJoinPoint point) {
        try {
            // 获取参数
            TypeToken<List<T>> typeToken = new TypeToken<List<T>>() {
            };
            Class<List<T>> clazz = (Class<List<T>>) typeToken.getRawType();

            // 获取参数
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

            HttpServletRequest request = attributes.getRequest();
            Integer pageNum = Optional.ofNullable(request.getParameter("pageNum")).map(Integer::valueOf).orElse(1);
            Integer pageSize = Optional.ofNullable(request.getParameter("pageSize")).map(Integer::valueOf).orElse(10);

            PageHelper.startPage(pageNum, pageSize);

            // 强制类型转换
            List<T> data = clazz.cast(point.proceed());

            Signature signature = point.getSignature();
            MethodSignature methodSignature = (MethodSignature) signature;
            Class<?> returnType = methodSignature.getReturnType();
            System.out.println("Return type: " + returnType.getName());

//            if (returnType.get().equals(ApiResult.class)){
//
//            }
            // 执行方法并获取结果
            ApiResult<List<T>> apiResult = (ApiResult<List<T>>) point.proceed();

            // 取出结果中的 list，并封装为 ListDataVo
            List<T> dataList = apiResult.getData();
            PageInfo<T> pageInfo = new PageInfo<>(dataList);
            return ResultUtil.success(new ListDataVo<>(dataList, pageInfo.getTotal()));

        } catch (Throwable e) {
            e.printStackTrace();
            throw new RuntimeException("分页出现异常，请检查参数，或者联系管理员");
        }
    }
}
