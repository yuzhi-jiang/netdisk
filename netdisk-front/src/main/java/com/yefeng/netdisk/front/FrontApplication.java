package com.yefeng.netdisk.front;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

/**
 * This class is for
 *
 * @author 夜枫
 * @version 2023-01-15 12:47
 */
@MapperScan("com.yefeng.netdisk.front.mapper")

//@MapperScan("com.yefeng.netdisk")
@SpringBootApplication(scanBasePackages ={"com.yefeng.netdisk","com.yefeng.hdfs.feign","com.yefeng.filestorage.feign"})
@EnableFeignClients(basePackages = {"com.yefeng.filestorage.feign","com.yefeng.hdfs.feign"})
@EnableWebMvc
@EnableAsync
public class FrontApplication {
    public static void main(String[] args) {
        SpringApplication.run(FrontApplication.class, args);
    }

}

//@MapperScan("com.baomidou.mybatisplus.samples.quickstart.mapper")
