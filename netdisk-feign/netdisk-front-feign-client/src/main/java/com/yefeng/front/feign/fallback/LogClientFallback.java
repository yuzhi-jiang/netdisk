package com.yefeng.front.feign.fallback;

import com.yefeng.front.feign.client.LogClient;
import feign.hystrix.FallbackFactory;


/**
 * This class is for
 *
 * @author 夜枫
 * @version 2023-01-15 15:28
 */
public class LogClientFallback implements FallbackFactory<LogClient> {
    @Override
    public LogClient create(Throwable cause) {
        return null;
    }
}
