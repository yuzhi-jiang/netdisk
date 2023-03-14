package com.yefeng.netdisk.front.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yefeng.hdfs.feign.client.HdfsClient;
import com.yefeng.netdisk.common.result.ApiResult;
import com.yefeng.netdisk.common.result.HttpCodeEnum;
import com.yefeng.netdisk.front.entity.User;
import com.yefeng.netdisk.front.mapper.UserMapper;
import com.yefeng.netdisk.front.service.IDiskService;
import com.yefeng.netdisk.front.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author yefeng
 * @since 2023-01-15
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {
    @Value("${mycloud.hdfsBasePath}")
    String hdfsBasePath;

    @Resource
    HdfsClient hdfsClient;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean uploadAvatar(MultipartFile avatarFile, String userId) {
        ApiResult apiResult = hdfsClient.uploadFile(hdfsBasePath, avatarFile);
        User user = new User();
        user.setId(Long.valueOf(userId));
        user.setImgPath(hdfsBasePath + '/' + avatarFile.getOriginalFilename());
        if (apiResult.getCode() == HttpCodeEnum.OK.getCode()) {
            return baseMapper.updateById(user) > 0;
        }
        return false;
    }

    public User getUserByThirdAuth(String uuid, User user) {

        User resUser = baseMapper.selectUserByInfo(uuid, user);

        return resUser;

    }

    @Autowired
    IDiskService diskService;

    @Transactional(rollbackFor = Exception.class)
    public boolean registerUserAndInitDisk(User user) {
        baseMapper.insert(user);
        diskService.initDisk(user.getId());
        return true;
    }
}
