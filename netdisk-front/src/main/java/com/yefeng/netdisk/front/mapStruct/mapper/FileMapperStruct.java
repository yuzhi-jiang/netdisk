package com.yefeng.netdisk.front.mapStruct.mapper;

import com.yefeng.netdisk.common.constans.FileTypeEnum;
import com.yefeng.netdisk.front.entity.File;
import com.yefeng.netdisk.front.util.FileStatusEnum;
import com.yefeng.netdisk.front.vo.FileVo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
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
public interface FileMapperStruct {
    FileMapperStruct INSTANCE = Mappers.getMapper(FileMapperStruct.class);
    @Mappings({
            @Mapping(source = "type", target = "type",qualifiedByName = "mapType"),
            @Mapping(source = "status", target = "status",qualifiedByName = "mapStatus")
    })
    FileVo toDto(File file);


    @Named("mapStatus")
    default String mapStatus(byte status) {
        return FileStatusEnum.fromCode(status).getName();
    }

    @Named("mapType")
    default String mapType(byte type) {
        return FileTypeEnum.fromCode(type).getValue();
    }

}
