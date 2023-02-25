package com.yefeng.netdisk.front.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * This class is for
 *
 * @author 夜枫
 * @version 2023-02-25 13:23
 */
@Data
public class ShareVo implements Serializable {
    @JsonProperty("shareId")
    private Long id;

    
    private Long diskId;

    
    private String shareTitle;

    
    private String sharePwd;
    
    private String fullShareMsg;

    
    private Long expiredTime;

    
    private Byte isValid;
    
    
    String[] fileIdList;

    Date expiration;
}
