package com.yefeng.netdisk.front.bo;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@Data
public class BatchBodyBo {
    private String driveID;
    private String fileID;
    private String toDriveId;
    private String toParentFileId;

}