package com.yefeng.netdisk.front.aspect;

import cn.hutool.json.JSONUtil;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yefeng.netdisk.common.result.ApiResult;
import com.yefeng.netdisk.common.result.ResultUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.io.IOException;
import java.io.StringWriter;

/**
 * @author 夜枫
 */
@Slf4j
@RestControllerAdvice(basePackages = {"com.yefeng.netdisk.front.controller"})
public class ResponseControllerAdvice implements ResponseBodyAdvice<Object> {

    /**
     * 判断哪些接口需要进行返回值包装
     * 返回 true 才会执行 beforeBodyWrite 方法；返回 false 则不执行。
     */
    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        log.info("判断是否需要进行返回值包装");
        //如果接口方法返回 Result 不需要再次包装
        //如果接口方法使用了 @NotResultWrap 注解，表示不需要包装了
        //只对成功的请求进行返回包装，异常情况统一放在全局异常中进行处理
        return true;
//        return !(returnType.getParameterType().equals(Result.class));
    }
    @Autowired
    ObjectMapper objectMapper;
    /**
     * 进行接口返回值包装
     */
    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
                                  Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                  ServerHttpRequest request, ServerHttpResponse response) {
        log.info("进行返回值包装");
        ApiResult<Object> objectApiResult = new ApiResult<>();

        if (returnType.getParameterType().equals(ApiResult.class)){
            JsonFactory factory = objectMapper.getFactory();
            JsonGenerator generator = null;
            try {
                String jsonString =   objectMapper.writeValueAsString(body);
                generator = factory.createGenerator(new StringWriter());
                generator.writeRawValue(jsonString);
                generator.flush();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            String unescapedJsonString = generator.getOutputTarget().toString();
            return JSONUtil.parseObj(unescapedJsonString);
        }

        //String 类型不能直接包装，需要进行特殊处理
        if (returnType.getParameterType().equals(String.class)){
//            ObjectMapper objectMapper = new ObjectMapper();
            try {
                //使用 jackson 将返回数据转换为 json
                objectMapper.getFactory().configure(JsonGenerator.Feature.ESCAPE_NON_ASCII, false);
                return objectMapper.writeValueAsString(ResultUtil.success(body));
            } catch (JsonProcessingException e) {
                //这里会走统一异常处理
                throw new RuntimeException("String类型返回值包装异常");
            }
        }
        try {
            String jsonString = objectMapper.writeValueAsString(ResultUtil.success(body));
            JsonFactory factory = objectMapper.getFactory();
            JsonGenerator generator = factory.createGenerator(new StringWriter());
            generator.writeRawValue(jsonString);
            generator.flush();
            String unescapedJsonString = generator.getOutputTarget().toString();
            return JSONUtil.parseObj(unescapedJsonString);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}