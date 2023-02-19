package com.yefeng.hdfs.feign.fallback;

import com.yefeng.hdfs.feign.client.HdfsClient;
import com.yefeng.netdisk.common.result.ApiResult;
import com.yefeng.netdisk.common.result.HttpCodeEnum;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

/**
 * This class is for
 *
 * @author 夜枫
 * @version 2023-01-15 15:28
 */
@Component
public class HdfsClientFallback implements FallbackFactory<HdfsClient> {
    @Override
    public HdfsClient create(Throwable cause) {
        cause.getCause().printStackTrace();
        System.out.println(cause.getMessage());
       return new HdfsClient() {
           @Override
           public ApiResult uploadFile(String path, MultipartFile file) {
               return new ApiResult(HttpCodeEnum.INTERNAL_SERVER_ERROR.getCode(), "哎呀呀~文件上传服务掉线了，请稍后再试吧！");
           }

           @Override
           public void downloadFile( String path, Boolean preview) {
//                return new ApiResult(HttpCodeEnum.INTERNAL_SERVER_ERROR.getCode(),HttpCodeEnum.INTERNAL_SERVER_ERROR.getMessage()+"请稍后再试");
               throw new RuntimeException(HttpCodeEnum.INTERNAL_SERVER_ERROR.getMessage()+"请稍后再试");
           }
       };
    }
}
