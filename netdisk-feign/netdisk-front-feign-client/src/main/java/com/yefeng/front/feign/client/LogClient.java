package com.yefeng.front.feign.client;

import com.yefeng.front.feign.fallback.LogClientFallback;
import com.yefeng.netdisk.common.result.ApiResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * This class is for
 *
 * @author 夜枫
 * @version 2023-01-15 15:25
 */
@FeignClient(value = "netdisk-cloud-account", fallback = LogClientFallback.class)
@Component
public interface LogClient {


    @GetMapping("/account/user/getinfo")
    public ApiResult getinfo(@RequestParam("info") String testInfo);

}
