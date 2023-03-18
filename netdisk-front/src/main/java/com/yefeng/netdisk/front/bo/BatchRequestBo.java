package com.yefeng.netdisk.front.bo;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;


@Data
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class BatchRequestBo {
    private BatchBodyBo body;

//    private String headers;
    private String fileId;
//    private String method;
//    private String url;

}
