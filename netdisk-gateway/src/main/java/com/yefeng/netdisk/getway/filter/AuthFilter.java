package com.yefeng.netdisk.getway.filter;

import cn.hutool.json.JSONObject;
import com.yefeng.netdisk.common.exception.BizException;
import com.yefeng.netdisk.common.exception.TokenException;
import com.yefeng.netdisk.common.result.ResultUtil;
import com.yefeng.netdisk.common.util.IPUtils;
import com.yefeng.netdisk.common.util.JWTUtil;
import com.yefeng.netdisk.common.util.PathUtil;
import com.yefeng.netdisk.common.validator.Assert;
import com.yefeng.netdisk.getway.config.WhitelistConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.function.Consumer;

/**
 * This class is for
 * 全局过滤器
 *
 * @author 夜枫
 * @version 2023-01-12 22:22
 */
@Component
@Slf4j
public class AuthFilter implements GlobalFilter, Ordered {

    @Resource
    private WhitelistConfig whitelistconfig;


    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        List<String> whiteList = whitelistconfig.getWhitelist();

        log.info("whiteList:{}", whiteList);

        ServerHttpRequest request = exchange.getRequest();

        String ipAddr = IPUtils.getIpAddress(request);


        String path = exchange.getRequest().getPath().toString();


        //url includes the parameters 包括参数
        URI uri = exchange.getRequest().getURI();


        log.info("the request ip:[{}] the URL is: [{}] ", ipAddr, uri);

        //the path is included in the whiteList
        boolean flag = whiteList.stream().anyMatch(pattern -> {
            return PathUtil.wildcardMatch(pattern, path);
        });
        if (flag) {
            log.info("the request ip:[{}] the URL is: [{}] is in the whiteList ", ipAddr, uri);
            return chain.filter(exchange);
        }

        if (whiteList.contains(path)) {
            return chain.filter(exchange);
        }


        String token = exchange.getRequest().getHeaders().getFirst("Authorization");
        log.info("token is  {}", token);

        try {

            Assert.isBlank(token, "需要先登录");
            JWTUtil.validateToken(token);


            Object subject = JWTUtil.getSubjectFromToken(token);
            Object[] payload = JWTUtil.getPayloadFromToken(token, "username");


            log.info("subject is {}", subject);

//
//            exchange.getAttributes().put("subject", subject);
//            if (payload != null && payload.length > 0) {
//                log.info("payload is {}", payload[0]);
//                exchange.getAttributes().put("username", payload[0]);
//            }
            // 追加请求头用户信息
            Consumer<HttpHeaders> httpHeaders = httpHeader -> {
                httpHeader.set("subject", (String) subject);
                if (payload != null && payload.length > 0) {

                    httpHeader.set("username", (String) payload[0]);
                }else{
                    httpHeader.set("username", "内网用户");
                }
            };
            ServerHttpRequest serverHttpRequest = exchange.getRequest()
                    .mutate()
                    .headers(httpHeaders)
                    .build();
            exchange.mutate().request(serverHttpRequest).build();

        } catch (BizException |TokenException ex) {
            log.warn(ex.getMessage());
            return response401(exchange.getResponse(), ResultUtil.custom(HttpStatus.UNAUTHORIZED.value(), ex.getMessage()));

        }



//        throw new BizException("你没有权限范围该路径，请先登录或者查看权限");

        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return 0;
    }

    private Mono<Void> response401(ServerHttpResponse response, Object jsonObject) {

//        byte[] bits = JSONObject.toJSONString(jsonObject).getBytes(StandardCharsets.UTF_8);
        JSONObject jsonObject1 = new JSONObject(jsonObject);
        byte[] bytes = jsonObject1.toString().getBytes(StandardCharsets.UTF_8);

        DataBuffer buffer = response.bufferFactory().wrap(bytes);
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        // 指定编码，否则在浏览器中会中文乱码
        response.getHeaders().add("Content-Type", "application/json;charset=UTF-8");
        return response.writeWith(Mono.just(buffer));
    }


}
