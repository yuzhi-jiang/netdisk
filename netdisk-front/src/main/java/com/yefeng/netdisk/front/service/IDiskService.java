package com.yefeng.netdisk.front.service;

import com.yefeng.netdisk.front.entity.Disk;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yefeng.netdisk.front.vo.DiskVo;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author yefeng
 * @since 2023-01-15
 */
public interface IDiskService extends IService<Disk> {
    /**
     *
     * 注册初始化云盘
     * @param userId
     * @return
     */
    Disk initDisk(Long userId);


    DiskVo getDiskInfoById(Long diskId );
    DiskVo getDiskInfoByUerId(Long userId );
}
