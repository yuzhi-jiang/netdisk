package com.yefeng.hadoop;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * This class is for
 *
 * @author 夜枫
 * @version 2023-01-01 14:00
 */
@SpringBootApplication
public class HadoopApplication {

   static Logger logger = LoggerFactory.getLogger(HadoopApplication.class);

    public static void main(String[] args) {
        logger.info("info");
        logger.warn("warn");
        logger.debug("debug");
        logger.error("error");
        SpringApplication.run(HadoopApplication.class, args);

    }
}
