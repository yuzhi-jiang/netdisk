package com.yefeng.netdisk.getway.filter;

import com.alibaba.fastjson.JSONObject;
import com.yefeng.netdisk.common.exception.BizException;
import com.yefeng.netdisk.common.result.ResultUtil;
import com.yefeng.netdisk.common.util.IPUtils;
import com.yefeng.netdisk.common.util.JWTUtil;
import com.yefeng.netdisk.common.validator.Assert;
import com.yefeng.netdisk.getway.config.WhitelistConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Set;

/**
 * This class is for
 *  全局过滤器
 * @author 夜枫
 * @version 2023-01-12 22:22
 */
@Component
@Slf4j
public class AuthFilter implements GlobalFilter, Ordered  {

    @Resource
    private WhitelistConfig whiteList;


    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        Set<String> whiteListSet = whiteList.getWhiteList();

        log.info("whiteListSet:{}",whiteListSet.toArray());

        ServerHttpRequest request = exchange.getRequest();

        String ipAddr = IPUtils.getIpAddress(request);


        String path = exchange.getRequest().getPath().toString();


        //url includes the parameters 包括参数
        URI uri = exchange.getRequest().getURI();



        log.info("the request ip:[{}] the URL is: [{}] ",ipAddr,uri);

        //the path is included in the whiteList
        if(whiteListSet.contains(path)) {
           return chain.filter(exchange);
        }
        String token = exchange.getRequest().getHeaders().getFirst("Authorization");


        try {

            Assert.isBlank(token,"需要先登录");
            JWTUtil.validateToken(token);
        }catch (BizException ex){
            log.warn(ex.getMessage());
            return response401(exchange.getResponse(), ResultUtil.custom(HttpStatus.UNAUTHORIZED.value(), ex.getMessage()));

        }

        log.info("token is  {}",token);




//        throw new BizException("你没有权限范围该路径，请先登录或者查看权限");

        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return 0;
    }

    private Mono<Void> response401(ServerHttpResponse response,Object jsonObject){

        byte[] bits = JSONObject.toJSONString(jsonObject).getBytes(StandardCharsets.UTF_8);
        DataBuffer buffer = response.bufferFactory().wrap(bits);
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        // 指定编码，否则在浏览器中会中文乱码
        response.getHeaders().add("Content-Type", "application/json;charset=UTF-8");
        return response.writeWith(Mono.just(buffer));
    }



}
