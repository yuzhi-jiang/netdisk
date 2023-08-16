package com.yefeng.filestorage.feign.client;

import com.yefeng.filestorage.feign.fallback.FileStroageClientFallback;
import com.yefeng.netdisk.common.result.ApiResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
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
@FeignClient(name = "file-storage", fallbackFactory = FileStroageClientFallback.class)
@Component
public interface FileStroageClient {

    /**
     * 上传文件
     *
     * @param file
     * @return
     */
    @PostMapping(value = "/file/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ApiResult uploadFile(
            @RequestPart("file")
            MultipartFile file,
            @RequestParam(value = "platfrom", defaultValue = "null") String platfrom
    );

    @GetMapping(value = "/file/get/path")
    void downloadFile(
            @RequestParam(name = "path") String path,
            @RequestParam(name = "preview", required = false) Boolean preview);

    @GetMapping(value = "/file/get/id")
    void downloadFileByFileId(
            @RequestParam(name = "fileId") String fileId,
            @RequestParam(name = "preview", required = false) Boolean preview);

}
