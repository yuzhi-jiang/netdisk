package com.yefeng.netdisk.front.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yefeng.netdisk.front.entity.File;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author yefeng
 * @since 2023-01-15
 */
public interface IFileService extends IService<File> {

    /**
     * 获取文件
     *
     * @param diskId
     * @param fileId
     * @return
     */
    File getFileWithDiskIdAndFileId(String diskId, String fileId);
}
