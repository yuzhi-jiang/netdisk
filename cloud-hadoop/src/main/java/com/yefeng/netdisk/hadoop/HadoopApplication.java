package com.yefeng.netdisk.hadoop;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * This class is for
 *
 * @author 夜枫
 * @version 2023-01-01 14:00
 */
@SpringBootApplication(scanBasePackages ="com.yefeng.netdisk")
@EnableAsync
public class HadoopApplication {

   static Logger logger = LoggerFactory.getLogger(HadoopApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(HadoopApplication.class, args);
    }
}
