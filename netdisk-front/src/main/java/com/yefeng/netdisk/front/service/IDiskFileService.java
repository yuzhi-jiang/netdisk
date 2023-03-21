package com.yefeng.netdisk.front.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yefeng.netdisk.front.dto.CreateFileDto;
import com.yefeng.netdisk.front.entity.DiskFile;
import com.yefeng.netdisk.front.util.FileStatusEnum;
import com.yefeng.netdisk.front.vo.DiskFileVo;

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

    List<DiskFileVo> getFileList(String diskId, String parentFileId, Byte status);

    boolean saveWithFileId(String diskId, String diskFileId, Long fileId);

    Object creatFolder(String diskId, String parentFileId, String fileName, String checkNameMode);


    DiskFile createFile(String diskId, String parentFileId, String fileName, String file_id, String checkNameMode);

    CreateFileDto creatFolder(DiskFile diskFile, String checkNameMode);

    DiskFile createFile(DiskFile diskFile, String checkNameMode);

    List<DiskFileVo> getPath(String diskId, String fileId);

    boolean deleteFile(String diskId, List<String> fileIds);

    boolean updateStatus(String diskId, List<String> fileIds, FileStatusEnum status);

    boolean moveFile(List<DiskFile> diskFiles);
}
