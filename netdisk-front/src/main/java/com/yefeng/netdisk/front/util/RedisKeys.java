package com.yefeng.netdisk.front.util;

/**
 * @author 夜枫
 */
public class RedisKeys {

    public static String getTokenKey(String key){

        return "token:"+ key;
    }
    public static String getCapatchaKey(String key){
        return "capatcha:"+ key;
    }

    public static String getUploadKey(String key){
        return "uploadid:"+ key;
    }

    public static String getForgetCapatchaKey(String key){
        return "forgetMobile:"+ key;
    }

    public static String getImageKey(){
        return "imageCapatcha:";
    }




}