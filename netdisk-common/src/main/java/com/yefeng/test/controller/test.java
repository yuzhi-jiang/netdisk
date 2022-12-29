package com.yefeng.test.controller;


import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

/**
 * This class is for
 *
 * @author 夜枫
 * @version 2022-12-28 20:05
 */
@RestController
@RequestMapping("/test")
public class test {

    /**
     * 测试接口
     *
     * @return
     */
    @RequestMapping("/info")
    public HashMap<String, String> test(){
        HashMap<String, String> map = new HashMap<>(1);
        map.put("id",String.valueOf(321));
        return map;
    }

}
