package com.yefeng.netdisk.front.service;

import com.yefeng.netdisk.front.bo.ShareBo;
import com.yefeng.netdisk.front.entity.DiskFile;
import com.yefeng.netdisk.front.entity.Share;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yefeng.netdisk.front.vo.ShareVo;

import java.util.List;

/**
 * <p>
 * 分享表 服务类
 * </p>
 *
 * @author yefeng
 * @since 2023-01-15
 */
public interface IShareService extends IService<Share> {

    ShareVo create(ShareBo shareBo);

    List<DiskFile> getFilesByShareId(String shareId, String parentFileId,Integer pageNum, Integer pageSize);

}
