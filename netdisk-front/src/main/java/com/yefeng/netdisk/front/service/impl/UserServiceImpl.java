package com.yefeng.netdisk.front.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yefeng.netdisk.front.entity.User;
import com.yefeng.netdisk.front.mapper.UserMapper;
import com.yefeng.netdisk.front.service.IUserService;
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
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

}
