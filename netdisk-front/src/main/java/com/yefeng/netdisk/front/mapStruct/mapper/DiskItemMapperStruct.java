package com.yefeng.netdisk.front.mapStruct.mapper;

import com.yefeng.netdisk.common.constans.FileTypeEnum;
import com.yefeng.netdisk.front.entity.DiskItem;
import com.yefeng.netdisk.front.util.FileStatusEnum;
import com.yefeng.netdisk.front.vo.DiskItemVo;
import org.mapstruct.Mapper;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

/**
 * This class is for
 *
 * @author 夜枫
 * @version 2023-03-05 12:31
 */
//@Mapper(componentModel = "spring")
@Mapper
public interface DiskItemMapperStruct {
    DiskItemMapperStruct INSTANCE = Mappers.getMapper(DiskItemMapperStruct.class);

    DiskItemVo toVo(DiskItem diskItem);



    @Named("mapStatus")
    default String mapStatus(byte status) {
        return FileStatusEnum.fromCode(status).getName();
    }

    @Named("mapType")
    default String mapType(byte type) {
        return FileTypeEnum.fromCode(type).getValue();
    }

}
