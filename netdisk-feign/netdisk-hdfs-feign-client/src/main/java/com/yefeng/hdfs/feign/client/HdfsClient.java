package com.yefeng.hdfs.feign.client;

import com.yefeng.hdfs.feign.fallback.HdfsClientFallback;
import com.yefeng.netdisk.common.result.ApiResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

/**
 * This class is for
 *
 * @author 夜枫
 * @version 2023-01-15 15:25
 */
@FeignClient(contextId ="hdfsUpload", value = "cloud-netdisk-hadoop", fallbackFactory  = HdfsClientFallback.class)
@Component
public interface HdfsClient {

    /**
     * 上传文件
     * @param path
     * @param file
     * @return
     */
    @PostMapping(value = "/hdfs/api/v1/hdfs/file",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ApiResult uploadFile(
            @RequestParam("path")
            String path,
            @RequestPart( "file")
            MultipartFile file
    );

}
