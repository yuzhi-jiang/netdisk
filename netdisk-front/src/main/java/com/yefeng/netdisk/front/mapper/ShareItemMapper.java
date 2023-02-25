package com.yefeng.netdisk.front.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yefeng.netdisk.front.entity.ShareItem;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author 夜枫
 */
public interface ShareItemMapper extends BaseMapper<ShareItem> {
    /**
     * 批量插入
     * @param shareItems
     * @return
     */
    Integer insertShareItems(@Param("shareItems") List<ShareItem> shareItems);

}
