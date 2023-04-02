package com.yefeng.netdisk.front.aspect;

import com.yefeng.netdisk.common.util.HttpContextUtils;
import com.yefeng.netdisk.common.util.JWTUtil;
import com.yefeng.netdisk.front.annotation.CreateBy;
import com.yefeng.netdisk.front.annotation.CreateTime;
import com.yefeng.netdisk.front.annotation.UpdateBy;
import com.yefeng.netdisk.front.annotation.UpdateTime;
import com.yefeng.netdisk.front.entity.BaseEntity;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.binding.MapperMethod;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.List;


/**
 * 自定义 Mybatis 插件，自动设置 createTime 和 updatTime 的值。
 * 拦截 update 操作（添加和修改）
 */
@Component
@Intercepts({@Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class})})
@Slf4j
public class CustomInterceptor implements Interceptor {

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        MappedStatement mappedStatement = (MappedStatement) invocation.getArgs()[0];

        // 获取 SQL 命令
        SqlCommandType sqlCommandType = mappedStatement.getSqlCommandType();
        //只判断新增和修改
        if (SqlCommandType.INSERT.equals(sqlCommandType) || SqlCommandType.UPDATE.equals(sqlCommandType)) {
            // 获取参数
            Object parameter = invocation.getArgs()[1];
            //批量操作时
            if (parameter instanceof MapperMethod.ParamMap) {
                MapperMethod.ParamMap map = (MapperMethod.ParamMap) parameter;
                Object obj = map.get("list");
                List<?> list = (List<?>) obj;
                if (list != null) {
                    for (Object o : list) {
                        setParameter(o, sqlCommandType);
                    }
                }
            } else {
                setParameter(parameter, sqlCommandType);
            }
        }
        return invocation.proceed();
    }

    public void setParameter(Object parameter, SqlCommandType sqlCommandType) throws Throwable {


        String tokenByHeader = HttpContextUtils.getTokenByHeader();
        Object subjectFromToken = JWTUtil.getSubjectFromToken(tokenByHeader);

        //获取当前登录用户id
        Long createBy= Long.valueOf(subjectFromToken.toString());


        Class<?> aClass = parameter.getClass();
        Field[] declaredFields;
        //如果常用字段提取了公共类 BaseEntity
        //判断BaseEntity是否是父类
        if (BaseEntity.class.isAssignableFrom(aClass)) {
        // 获取父类私有成员变量
            declaredFields = aClass.getSuperclass().getDeclaredFields();
        } else {
            // 获取私有成员变量
            declaredFields = aClass.getDeclaredFields();
        }
        for (Field field : declaredFields) {
            if (SqlCommandType.INSERT.equals(sqlCommandType)) { // insert 语句插入 createBy
                if (field.getAnnotation(CreateBy.class) != null) {
                    field.setAccessible(true);
                    field.set(parameter, createBy);
                }

                if (field.getAnnotation(CreateTime.class) != null) { // insert 语句插入 createTime
                    field.setAccessible(true);
                    field.set(parameter, LocalDateTime.now());
                }
            }

            if (SqlCommandType.UPDATE.equals(sqlCommandType)) {
                if (field.getAnnotation(UpdateTime.class) != null) { // update 语句插入 updateTime
                    field.setAccessible(true);
                    field.set(parameter, LocalDateTime.now());
                }
                if (field.getAnnotation(UpdateBy.class) != null) { // update 语句插入 updateBy
                    field.setAccessible(true);
                    field.set(parameter, createBy);
                }
            }
        }
    }
}
