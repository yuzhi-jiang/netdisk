package com.yefeng.netdisk.front.mapper;

import com.yefeng.netdisk.front.entity.File;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author yefeng
 * @since 2023-01-15
 */
public interface FileMapper extends BaseMapper<File> {

    /**
     * join查找File
     * @param diskId
     * @param fileId
     * @return
     */
    File selectFileWithDiskIdAndFileId(@Param("disk_id") String diskId,@Param("file_id") String fileId);
}
