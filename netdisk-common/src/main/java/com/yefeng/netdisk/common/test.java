package com.yefeng.netdisk.common;

import cn.hutool.crypto.digest.BCrypt;

/**
 * This class is for
 *
 * @author 夜枫
 * @version 2023-01-07 17:43
 */
public class test {
    static String gensalt="$2a$10$wOx7KHziMDOlrEYhdbT0MO";
    public static void main(String[] args) {

        String password="jiangshao";



        String hashpw = BCrypt.hashpw(password, gensalt);
        System.out.println(hashpw);

        System.out.println("密码匹配吗："+(BCrypt.checkpw(password, hashpw)==true?"匹配":"不匹配"));


    }
}
