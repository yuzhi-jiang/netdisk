package com.yefeng.netdisk.front.service.impl;

import com.yefeng.netdisk.front.entity.File;
import com.yefeng.netdisk.front.mapper.FileMapper;
import com.yefeng.netdisk.front.service.IFileService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author yefeng
 * @since 2023-01-15
 */
@Service
public class FileServiceImpl extends ServiceImpl<FileMapper, File> implements IFileService {

}
