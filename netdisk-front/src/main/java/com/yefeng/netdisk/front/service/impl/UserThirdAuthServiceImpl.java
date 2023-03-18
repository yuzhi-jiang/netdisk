package com.yefeng.netdisk.front.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yefeng.netdisk.front.entity.User;
import com.yefeng.netdisk.front.entity.UserThirdAuth;
import com.yefeng.netdisk.front.mapper.UserThirdAuthMapper;
import com.yefeng.netdisk.front.service.IUserService;
import com.yefeng.netdisk.front.service.IUserThirdAuthService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author yefeng
 * @since 2023-01-15
 */
@Service
public class UserThirdAuthServiceImpl extends ServiceImpl<UserThirdAuthMapper, UserThirdAuth> implements IUserThirdAuthService {


    @Resource
    IUserService userService;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Boolean registerOauthUser(User user, UserThirdAuth userThirdAuth) {

        userService.registerUserAndInitDisk(user);
        userThirdAuth.setUserId(user.getId());
        baseMapper.insert(userThirdAuth);
        return true;
    }
}
