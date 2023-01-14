package com.yefeng.netdisk.getway.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.HashSet;
import java.util.Set;

/**
 * This class is for
 * 过滤器白名单
 * @author 夜枫
 * @version 2023-01-13 11:07
 */

@Component
@RefreshScope
public class WhitelistConfig {


    @Value("${system.whitelist}")

    String whiteListStr;



    public String getWhiteListStr() {
        return whiteListStr;
    }

    public void setWhiteListStr(String whiteListStr) {
        this.whiteListStr = whiteListStr;
    }

    public Set<String> getWhiteList() {
        String whitelists = this.whiteListStr;
        if (StringUtils.isEmpty(whitelists)) {
            return new HashSet<>();
        }
        Set<String> whiteList = new HashSet<>();
        String[] whiteArray = whitelists.split(",");
        for (int i = 0; i < whiteArray.length; i++) {
            String str = whiteArray[i];
            whiteList.add(whiteArray[i]);
        }
        return whiteList;
    }


}
