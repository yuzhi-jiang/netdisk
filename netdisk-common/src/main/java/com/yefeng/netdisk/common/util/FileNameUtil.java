package com.yefeng.netdisk.common.util;

import cn.hutool.core.io.FileUtil;

/**
 * This class is for
 *
 * @author 夜枫
 * @version 2023-01-06 23:40
 */
public class FileNameUtil {
    public static String getFileNameByPath(String path) {
        return FileUtil.getName(path);
    }

    public static void main(String[] args) {
        String name = getFileNameByPath("E:\\Videos\\爬虫\\day1\\20220920_200037.mp4");
        String name1 = getFileNameByPath(name);
        System.out.println("name: " + name);
        System.out.println("name1: " + name1);

    }

}
