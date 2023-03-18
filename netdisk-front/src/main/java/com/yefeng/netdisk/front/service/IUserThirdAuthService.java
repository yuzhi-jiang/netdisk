package com.yefeng.netdisk.front.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yefeng.netdisk.front.entity.User;
import com.yefeng.netdisk.front.entity.UserThirdAuth;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author yefeng
 * @since 2023-01-15
 */
public interface IUserThirdAuthService extends IService<UserThirdAuth> {


    /**
     * 第三方登录，默认创建用户
     * @param user
     * @param userThirdAuth
     * @return
     */
    public Boolean registerOauthUser(User user, UserThirdAuth userThirdAuth);
}
