package com.yefeng.netdisk.front.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yefeng.netdisk.front.bo.FileBo;
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

    List<FileBo> getFileList(@Param("diskId") String diskId,@Param("parentFileId") String parentFileId);

}
