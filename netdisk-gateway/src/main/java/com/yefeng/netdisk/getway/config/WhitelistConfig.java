package com.yefeng.netdisk.getway.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * This class is for
 * 过滤器白名单
 * @author 夜枫
 * @version 2023-01-13 11:07
 */

@Component
@RefreshScope
@ConfigurationProperties(prefix = "system")
public class WhitelistConfig {


//    @Value("${system.whitelist}")
//
//    String whiteListStr;


    String[] whitelist;

    public void setWhitelist(String[] whitelist) {
        this.whitelist = whitelist;
    }

    public List<String> getWhitelist() {
        HashSet<String> hashSet = new HashSet<>(List.of(whitelist));
        return new ArrayList<>(hashSet);
    }



//    public String getWhiteListStr() {
//        return whiteListStr;
//    }
//
//    public void setWhiteListStr(String whiteListStr) {
//        this.whiteListStr = whiteListStr;
//    }
//
//    public Set<String> getWhiteList() {
//        String whitelists = this.whiteListStr;
//        if (StringUtils.isEmpty(whitelists)) {
//            return new HashSet<>();
//        }
//        Set<String> whiteList = new HashSet<>();
//        String[] whiteArray = whitelists.split(",");
//        for (int i = 0; i < whiteArray.length; i++) {
//            String str = whiteArray[i];
//            whiteList.add(whiteArray[i]);
//        }
//        return whiteList;
//    }


}
