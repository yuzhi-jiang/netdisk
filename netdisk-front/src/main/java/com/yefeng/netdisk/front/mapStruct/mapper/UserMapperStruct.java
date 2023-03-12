package com.yefeng.netdisk.front.mapStruct.mapper;

import com.yefeng.netdisk.front.entity.User;
import com.yefeng.netdisk.front.vo.UserVo;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * This class is for
 *
 * @author 夜枫
 * @version 2023-03-05 12:31
 */
@Mapper
public interface UserMapperStruct {
    UserMapperStruct INSTANCE = Mappers.getMapper(UserMapperStruct.class);

    UserVo toDto(User user);



}
