package com.yefeng.netdisk.front.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yefeng.netdisk.front.entity.DiskFile;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author yefeng
 * @since 2023-01-15
 */
public interface DiskFileMapper extends BaseMapper<DiskFile> {

    List<DiskFile> getFileList(@Param("diskId") String diskId,@Param("parentFileId") String parentFileId);
    List<DiskFile> selectFilePathByDiskIDAndFileId(@Param("diskId") Long diskId,@Param("disk_file_id") String fileId);

    int deleteFile(String diskId, List<String> fileIds);

    int updateStatus(String diskId, List<String> fileIds, byte statusCode);


    int moveFileBatch(@Param("diskFiles") List<DiskFile> diskFiles);

}
