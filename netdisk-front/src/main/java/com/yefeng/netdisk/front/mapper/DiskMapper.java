package com.yefeng.netdisk.front.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yefeng.netdisk.front.entity.Disk;
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
public interface DiskMapper extends BaseMapper<Disk> {

    /**
     * 修改容量
     * @param diskId
     * @param type
     * @param capacity
     * @return
     */
    boolean updateCapacity(@Param("diskId") Long diskId,@Param("type") Integer type,@Param("capacity") Long capacity);

}
