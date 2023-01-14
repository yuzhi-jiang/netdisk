package com.yefeng.netdisk.common.service;

import com.yefeng.netdisk.common.entity.SysLogEntity;

import java.util.List;


/**
 * 系统日志
 * @author 夜枫
 */
public interface ISysLogService  {

//    PageUtils queryPage(Map<String, Object> params);

    boolean deleteAll();

    List<SysLogEntity> getIndexInterfaces();
}
