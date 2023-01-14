package com.yefeng.netdisk.common;

import cn.hutool.core.io.FileUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is for
 *
 * @author 夜枫
 * @version 2023-01-07 17:43
 */
public class test {
    public static void main(String[] args) {
        String filename="aa.html";
        List<String> list= new ArrayList<String>();
        list.add("aa.txt");
        list.add("aa.html");
        list.add("aa.mp4");
        list.add("aa.mp3");
        list.add("aa.jpg");
        list.add("aa.png");

        list.forEach(it-> System.out.println(
                FileUtil.getMimeType(it)
        ));


    }
}
