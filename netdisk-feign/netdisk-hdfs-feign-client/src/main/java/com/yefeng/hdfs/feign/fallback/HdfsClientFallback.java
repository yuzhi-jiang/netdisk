package com.yefeng.hdfs.feign.fallback;

import com.yefeng.hdfs.feign.client.HdfsClient;
import com.yefeng.netdisk.common.result.ApiResult;
import com.yefeng.netdisk.common.result.HttpCodeEnum;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

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
        return (path, file) -> new ApiResult(HttpCodeEnum.INTERNAL_SERVER_ERROR.getCode(),"哎呀呀~文件上传服务掉线了，请稍后再试吧！");
    }
}
