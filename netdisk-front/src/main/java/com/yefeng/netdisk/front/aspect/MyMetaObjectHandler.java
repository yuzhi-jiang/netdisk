package com.yefeng.netdisk.front.aspect;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.yefeng.netdisk.common.util.HttpContextUtils;
import com.yefeng.netdisk.common.util.JWTUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Component // 一定不要忘记把处理器加到IOC容器中！
public class MyMetaObjectHandler implements MetaObjectHandler {
    // 插入时的填充策略
    @Override
    public void insertFill(MetaObject metaObject) {
        System.out.println(metaObject.toString());
//        log.info("start insert fill.....");
        // setFieldValByName(String fieldName, Object fieldVal, MetaObject
        LocalDateTime now = LocalDateTime.now();
        this.setFieldValByName("createTime", now,metaObject);
        this.setFieldValByName("modifyTime",now,metaObject);
        String token = HttpContextUtils.getTokenByHeader();
        if(StringUtils.isNotBlank(token)){
            JWTUtil.validateToken(token);
            Object subjectFromToken = JWTUtil.getSubjectFromToken(token);

            //获取当前登录用户id
            Long createBy= Long.valueOf(subjectFromToken.toString());
            this.setFieldValByName("createUser",createBy,metaObject);
        }
    }
    // 更新时的填充策略
    @Override
    public void updateFill(MetaObject metaObject) {
//        log.info("start update fill.....");
        String token = HttpContextUtils.getTokenByHeader();
        if(StringUtils.isNotBlank(token)){
            JWTUtil.validateToken(token);
            Object subjectFromToken = JWTUtil.getSubjectFromToken(token);
            //获取当前登录用户id
            Long createBy= Long.valueOf(subjectFromToken.toString());
            this.setFieldValByName("modifyUser",createBy,metaObject);
        }
        this.setFieldValByName("modifyTime",LocalDateTime.now(),metaObject);
    }
}