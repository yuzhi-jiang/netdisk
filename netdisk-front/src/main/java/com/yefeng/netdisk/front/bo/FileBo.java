package com.yefeng.netdisk.front.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;



/**
 * This class is for
 *
 * @author 夜枫
 * @version 2023-01-23 14:04
 */
@Data
public class FileBo {

    @ApiModelProperty("云盘id")
    private String diskId;


    @ApiModelProperty("用户文件的名称（同样的文件每个人文件有不同的名称）")
    private String fileName;

    @ApiModelProperty("用户文件的父文件夹名称（同样的文件每个人文件有不同的路径）")
    private String parentFileId;


    @ApiModelProperty("名称处理")
    private String checkNameMode;

    @ApiModelProperty("文件类型")
    private String type;

    @ApiModelProperty("文件hash")
    private String hash;



}
