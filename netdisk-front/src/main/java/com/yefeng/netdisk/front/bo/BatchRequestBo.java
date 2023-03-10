package com.yefeng.netdisk.front.bo;

import lombok.Data;

@Data
public class BatchRequestBo {
    private BatchBodyBo body;

    private String headers;
    private String id;
    private String method;
    private String url;

}
