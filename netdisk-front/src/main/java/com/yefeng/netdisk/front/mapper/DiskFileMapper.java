package com.yefeng.netdisk.front.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yefeng.netdisk.front.dto.DiskFileDto;
import com.yefeng.netdisk.front.entity.DiskFile;
import org.apache.ibatis.annotations.Param;

import java.lang.reflect.Array;
import java.util.ArrayList;
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

    List<DiskFileDto> getFileList(@Param("diskId") String diskId, @Param("parentFileId") String parentFileId,@Param("status") Byte status,@Param("search") String search);
    List<DiskFile> selectFilePathByDiskIDAndFileId(@Param("diskId") Long diskId,@Param("disk_file_id") String fileId);
    List<DiskFile> selectAllSubDiskFile(@Param("diskId") Long diskId,@Param("diskFileIds") List<String> diskFileIds);

    int deleteFile(String diskId, List<String> fileIds);

    int updateStatus(String diskId, List<String> fileIds, byte statusCode);


    int moveFileBatch(@Param("diskFiles") List<DiskFile> diskFiles);

    int updateBatchByIds(List<DiskFile> diskFiles);

}
