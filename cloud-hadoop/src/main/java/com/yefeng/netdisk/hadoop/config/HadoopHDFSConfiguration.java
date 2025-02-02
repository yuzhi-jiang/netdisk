package com.yefeng.netdisk.hadoop.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.hadoop.fs.FileSystem;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * @author 夜枫
 */
@Slf4j
@Configuration
public class HadoopHDFSConfiguration {

    @Value("${hdfs.hdfsPath}")
    private String hdfsPath;
    @Value("${hdfs.hdfsName}")
    private String hdfsName;

    @Bean
    public org.apache.hadoop.conf.Configuration  getConfiguration(){
        org.apache.hadoop.conf.Configuration configuration = new org.apache.hadoop.conf.Configuration();
        configuration.set("fs.defaultFS", hdfsPath);
        return configuration;
    }

    @Bean(name = "FileSystem")
    public FileSystem getFileSystem(){
        FileSystem fileSystem = null;
        try {
            fileSystem = FileSystem.get(new URI(hdfsPath), getConfiguration(), hdfsName);
        } catch (IOException | InterruptedException | URISyntaxException e) {
            // TODO Auto-generated catch block
            log.error(e.getMessage());
        }
        return fileSystem;
    }

}
