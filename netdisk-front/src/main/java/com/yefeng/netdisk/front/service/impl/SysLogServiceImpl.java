package com.yefeng.netdisk.front.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yefeng.netdisk.front.entity.SysLog;
import com.yefeng.netdisk.front.mapper.SysLogMapper;
import com.yefeng.netdisk.front.service.ISysLogService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 * 系统日志 服务实现类
 * </p>
 *
 * @author yefeng
 * @since 2023-01-15
 */
@Service
public class SysLogServiceImpl extends ServiceImpl<SysLogMapper, SysLog> implements ISysLogService {

    @Resource
    SysLogMapper sonLogMapper;

    @Override
    public void insert(SysLog sysLog) {
        sonLogMapper.insert(sysLog);
    }
}
