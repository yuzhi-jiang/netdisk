package com.yefeng.netdisk.front.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yefeng.netdisk.front.bo.FileBo;
import com.yefeng.netdisk.front.entity.DiskFile;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author yefeng
 * @since 2023-01-15
 */
public interface IDiskFileService extends IService<DiskFile> {
    List<FileBo> getFileList(String diskId, String parentFileId);

}
