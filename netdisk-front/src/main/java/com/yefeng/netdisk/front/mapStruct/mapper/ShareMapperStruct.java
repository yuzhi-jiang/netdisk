package com.yefeng.netdisk.front.mapStruct.mapper;

import com.yefeng.netdisk.common.constans.FileTypeEnum;
import com.yefeng.netdisk.front.dto.ShareItemDto;
import com.yefeng.netdisk.front.entity.Share;
import com.yefeng.netdisk.front.util.FileStatusEnum;
import com.yefeng.netdisk.front.vo.ShareItemVo;
import com.yefeng.netdisk.front.vo.ShareVo;
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
public interface ShareMapperStruct {
    ShareMapperStruct INSTANCE = Mappers.getMapper(ShareMapperStruct.class);
    @Mappings({
            @Mapping(source = "type", target = "type",qualifiedByName = "mapType"),
            @Mapping(source = "sharePwd", target = "hasPwd",qualifiedByName = "mapHasPwd")
    })
    ShareVo toVo(Share share);
    @Mappings({
            @Mapping(source = "type", target = "type",qualifiedByName = "mapType"),
            @Mapping(source = "diskFileId", target = "fileId")
    })
    ShareItemVo itemDtotoVo(ShareItemDto shareItemDto);
    @Named("mapHasPwd")
    default Boolean hasPwd(String sharePwd) {
        return sharePwd != null && !sharePwd.equals("");
    }
    @Named("mapStatus")
    default String mapStatus(byte status) {
        return FileStatusEnum.fromCode(status).getName();
    }

    @Named("mapType")
    default String mapType(byte type) {
        return FileTypeEnum.fromCode(type).getValue();
    }

}
