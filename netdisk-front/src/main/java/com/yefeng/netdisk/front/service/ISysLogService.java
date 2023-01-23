package com.yefeng.netdisk.front.service;

import com.yefeng.netdisk.front.entity.SysLog;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 系统日志 服务类
 * </p>
 *
 * @author yefeng
 * @since 2023-01-15
 */
public interface ISysLogService extends IService<SysLog> {

    void insert(SysLog sysLog);
}
