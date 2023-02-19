package com.yefeng.netdisk.front.service;

import com.yefeng.netdisk.front.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author yefeng
 * @since 2023-01-15
 */
public interface IUserService extends IService<User> {

    /**
     * 头像上传
     * @param avatarFile
     * @param userId
     * @return
     */
    Boolean uploadAvatar(MultipartFile avatarFile, String userId);

}
