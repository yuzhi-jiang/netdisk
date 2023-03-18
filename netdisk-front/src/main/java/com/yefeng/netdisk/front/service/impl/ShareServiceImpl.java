package com.yefeng.netdisk.front.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.yefeng.netdisk.common.validator.Assert;
import com.yefeng.netdisk.front.bo.ShareBo;
import com.yefeng.netdisk.front.entity.DiskFile;
import com.yefeng.netdisk.front.entity.Share;
import com.yefeng.netdisk.front.entity.ShareItem;
import com.yefeng.netdisk.front.mapper.DiskFileMapper;
import com.yefeng.netdisk.front.mapper.ShareItemMapper;
import com.yefeng.netdisk.front.mapper.ShareMapper;
import com.yefeng.netdisk.front.service.IShareService;
import com.yefeng.netdisk.front.vo.ShareVo;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

    @Resource
    ShareItemMapper shareItemMapper;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ShareVo create(ShareBo shareBo) {
        Share share = new Share();
        Long diskId = Long.valueOf(shareBo.getDiskId());
        share.setDiskId(diskId);
        String firstFileId = shareBo.getFileIdList()[0];

        DiskFile diskFile = diskFileMapper.selectOne(new QueryWrapper<DiskFile>().eq("disk_file_id", firstFileId));

        Assert.isNull(diskFile, "文件不存在");

        String shareTitle = diskFile.getFileName();

        int shareSize = shareBo.getFileIdList().length;
        if (shareSize > 1) {
            shareTitle += "等文件";
        }
        share.setType(shareBo.getType());
        share.setSharePwd(shareBo.getSharePwd());
        share.setShareTitle(shareTitle);
        share.setFullShareMsg(shareTitle);
        share.setExpiredTime(shareBo.getExpiration());

        baseMapper.insert(share);


        List<ShareItem> shareItems = new ArrayList<>(shareSize);
        for (int i = 0; i < shareSize; i++) {
            ShareItem shareItem = new ShareItem();

            shareItem.setFileId(shareBo.getFileIdList()[i]);
            shareItem.setType(shareBo.getType());
            shareItem.setDiskId(Long.valueOf(shareBo.getDiskId()));
            shareItem.setShareId(share.getId());

            shareItems.add(shareItem);
        }


        Integer integer = shareItemMapper.insertShareItems(shareItems);

        if (integer == shareSize) {
            ShareVo shareVo = new ShareVo();
            BeanUtils.copyProperties(share, shareVo);
            shareVo.setFileIdList(shareBo.getFileIdList());
            return shareVo;
        }

        log.warn("分享发生了错误");
        log.warn(shareBo.toString());
        throw new RuntimeException("分享失败~请稍后再试");
    }

    @Resource
    DiskFileMapper diskFileMapper;

    public List<DiskFile> getFilesByShareId(String shareId, String parentFileId, @RequestParam("page_num") Integer pageNum, @RequestParam("page_size") Integer pageSize) {
        QueryWrapper<ShareItem> queryWrapper = new QueryWrapper<ShareItem>().eq("share_id", shareId);

//        Page<ShareItem> shareItemPage = shareItemMapper.selectPage(new Page<>(pageNum, pageSize), queryWrapper);
//
//        List<ShareItem> shareItems = shareItemPage.getRecords();

        PageHelper.startPage(pageNum,pageSize);
        List<ShareItem> shareItems=  shareItemMapper.selectList(queryWrapper);


        Long diskId = shareItems.get(0).getDiskId();

        List<String> fileIds = shareItems.stream().map(ShareItem::getFileId).collect(Collectors.toList());


        List<DiskFile> diskFiles = diskFileMapper.selectList(new QueryWrapper<DiskFile>().eq("disk_id", diskId)
                .eq("parent_file_id", parentFileId));


        //todo  可能过滤后少于需要的，也就是正确的没有查到，查到的不够，是需要结果limit，不是过滤前的limit
        List<DiskFile> resFiles = diskFiles.stream()
                .filter(df -> fileIds.contains(df.getDiskFileId()))
                .collect(Collectors.toList());

        return resFiles;
    }
}
