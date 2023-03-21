package com.yefeng.netdisk.front.bo;

import lombok.Data;


@Data
public class BatchBodyBo {
    private String diskId;
    private String fileID;
    private String toDriveId;
    private String toParentFileId;

}