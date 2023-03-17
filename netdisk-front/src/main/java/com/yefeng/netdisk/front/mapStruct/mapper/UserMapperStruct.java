package com.yefeng.netdisk.front.mapStruct.mapper;

import com.yefeng.netdisk.front.entity.User;
import com.yefeng.netdisk.front.util.UserStatusEnum;
import com.yefeng.netdisk.front.vo.UserVo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
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


//    @Mappings({
//            @Mapping(source = "status", target = "status",qualifiedByName = "mapStatus")
//    })
    @Mapping(source = "id", target = "userid")
    UserVo toDto(User user);


    @Named("mapStatus")
    default String mapStatus(byte status) {
       return status==UserStatusEnum.NORMAL? "正常": "禁用";
    }
}
