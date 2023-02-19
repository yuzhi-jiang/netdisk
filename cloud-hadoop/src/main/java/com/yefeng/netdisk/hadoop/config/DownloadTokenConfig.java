package com.yefeng.netdisk.hadoop.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

/**
 * This class is for
 *
 * @author 夜枫
 * @version 2023-02-19 22:06
 */
@Component
@RefreshScope
public class DownloadTokenConfig {
    @Value("${mycloud.downloadToken.enable:true}")
    Boolean enableValidateToken;

    @Value("${mycloud.downloadToken.position:header}")
    String tokenPosition;

    public Boolean getEnableValidateToken() {
        return enableValidateToken;
    }

    public void setEnableValidateToken(Boolean enableValidateToken) {
        this.enableValidateToken = enableValidateToken;
    }

    public void setTokenPosition(String tokenPosition) {
        this.tokenPosition = tokenPosition;
    }

    public String getTokenPosition() {
        return tokenPosition;
    }
}
