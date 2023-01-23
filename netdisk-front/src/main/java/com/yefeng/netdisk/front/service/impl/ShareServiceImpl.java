package com.yefeng.netdisk.front.service.impl;

import com.yefeng.netdisk.front.entity.Share;
import com.yefeng.netdisk.front.mapper.ShareMapper;
import com.yefeng.netdisk.front.service.IShareService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 分享表 服务实现类
 * </p>
 *
 * @author yefeng
 * @since 2023-01-15
 */
@Service
public class ShareServiceImpl extends ServiceImpl<ShareMapper, Share> implements IShareService {

}
