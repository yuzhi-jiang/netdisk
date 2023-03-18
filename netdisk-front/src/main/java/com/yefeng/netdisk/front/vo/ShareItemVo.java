package com.yefeng.netdisk.front.vo;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.io.Serializable;

/**
 * This class is for
 *
 * @author 夜枫
 * @version 2023-02-25 15:51
 */
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@Data
public class ShareItemVo implements Serializable {
//    {
//        "drive_id": "358565",
//
//            "file_id": "62f9ac1deb190306e1414e4c9fe043823fbeb6b4",
//            "share_id": "xSX2h1D5RHL",
//            "name": "java基础",
//            "type": "folder",
//            "created_at": "2022-08-15T02:14:53.893Z",
//            "updated_at": "2022-08-15T02:14:53.893Z",
//            "parent_file_id": "62f90b6a2173e426e8ad40a081de4c54b179c838"
//    }



    private String driveId;
    private String fileId;
    private String shareId;
    private String name;
    private String type;
    private String parentFileId;


}
