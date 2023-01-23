package com.yefeng.netdisk.front.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yefeng.netdisk.front.bo.FileBo;
import com.yefeng.netdisk.front.entity.DiskFile;
import com.yefeng.netdisk.front.mapper.DiskFileMapper;
import com.yefeng.netdisk.front.service.IDiskFileService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author yefeng
 * @since 2023-01-15
 */
@Service
public class DiskFileServiceImpl extends ServiceImpl<DiskFileMapper, DiskFile> implements IDiskFileService {


    @Override
    public List<FileBo> getFileList(String diskId, String parentFileId) {
        return baseMapper.getFileList(diskId, parentFileId);
    }
}
