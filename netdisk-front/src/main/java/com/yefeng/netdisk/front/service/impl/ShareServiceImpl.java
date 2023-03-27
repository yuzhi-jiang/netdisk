package com.yefeng.netdisk.front.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yefeng.netdisk.common.constans.FileTypeEnum;
import com.yefeng.netdisk.common.exception.CheckFailException;
import com.yefeng.netdisk.common.validator.Assert;
import com.yefeng.netdisk.front.bo.ShareBo;
import com.yefeng.netdisk.front.dto.ShareItemDto;
import com.yefeng.netdisk.front.entity.DiskFile;
import com.yefeng.netdisk.front.entity.Share;
import com.yefeng.netdisk.front.entity.ShareItem;
import com.yefeng.netdisk.front.mapStruct.mapper.ShareMapperStruct;
import com.yefeng.netdisk.front.mapper.DiskFileMapper;
import com.yefeng.netdisk.front.mapper.ShareItemMapper;
import com.yefeng.netdisk.front.mapper.ShareMapper;
import com.yefeng.netdisk.front.service.IShareService;
import com.yefeng.netdisk.front.util.ShareStatusEnum;
import com.yefeng.netdisk.front.vo.ShareVo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
        if(!diskFile.getDiskId().equals(diskId)){
            throw new CheckFailException("文件不存在");
        }


        Assert.isNull(diskFile, "文件不存在");

        String shareTitle = diskFile.getFileName();

        int shareSize = shareBo.getFileIdList().length;
        if (shareSize > 1) {
            shareTitle += "等" + shareSize + "个文件";
        }

        if (shareBo.getType() != null && (shareBo.getType() == 1 || shareBo.getType() == 2)) {
            share.setType(shareBo.getType());
        }
        else{
            share.setType(FileTypeEnum.FILE.getCode());//默认为文件
            String[] fileIdList = shareBo.getFileIdList();
            List<Map<String, Object>> maps = diskFileMapper.selectMaps(new QueryWrapper<DiskFile>().select("type").in("disk_file_id", fileIdList));
            Long count=diskFileMapper.selectCount(new QueryWrapper<DiskFile>().eq("type",FileTypeEnum.FOLDER.getCode()).in("disk_file_id", fileIdList));
            if(count>0){
                share.setType(FileTypeEnum.FOLDER.getCode());
            }
//            for (Map<String, Object> map : maps) {
//                if (map.get("type").equals(FileTypeEnum.FOLDER.getCode())) {
//                    share.setType((byte) 2);
//                    break;
//                }
//            }
        }


        share.setDiskId(diskId);
        share.setIsValid(ShareStatusEnum.valid.getCode());

        share.setSharePwd(shareBo.getSharePwd());
        share.setShareTitle(shareTitle);
        share.setFullShareMsg(shareTitle);
        share.setExpiredTime(shareBo.getExpiredTime());

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
            ShareVo shareVo = ShareMapperStruct.INSTANCE.toVo(share);
            shareVo.setShareUrl(shareBo.getShareUrl() + "/" + share.getId());
            shareVo.setFileIdList(shareBo.getFileIdList());
            return shareVo;
        }

        log.warn("分享发生了错误");
        log.warn(shareBo.toString());
        throw new RuntimeException("分享失败~请稍后再试");
    }
    @Resource
    DiskFileMapper diskFileMapper;

    public List<ShareItemDto> getFilesByShareId(String shareId, String parentFileId, @RequestParam("page_num") Integer pageNum, @RequestParam("page_size") Integer pageSize) {



        List<ShareItemDto> shareItemDtos;
        if (parentFileId.equals("root")) {
            shareItemDtos = shareItemMapper.selectListByShareId(shareId, null);
            shareItemDtos.forEach(shareItemDto -> {
                shareItemDto.setParentFileId("root");
            });
        } else {
            shareItemDtos = shareItemMapper.selectListByShareId(shareId, parentFileId);
        }
        return shareItemDtos;
    }
}
