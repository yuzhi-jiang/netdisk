package com.yefeng.netdisk.hadoop.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author big uncle
 * @date 2021/5/10 19:53
 * @module
 **/
@Data
@ConfigurationProperties(prefix = "hdfs.properties")
@Component
public class HDFSConfig {

    /**
     * hdfs 服务器地址
    **/
    private String hostname;
    /**
     * hdfs 服务器端口
    **/
    private String port;
    /**
     * hdfs 服务器账户
    **/
    private String username;

}