package com.yefeng.netdisk.front.mapper;

import com.yefeng.netdisk.front.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author yefeng
 * @since 2023-01-15
 */
public interface UserMapper extends BaseMapper<User> {

    User selectUserByInfo(@Param("uuid") String uuid, @Param("user") User user);
}
