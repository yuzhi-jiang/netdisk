package com.yefeng.netdisk.common.util;

import cn.hutool.core.io.FileUtil;
import org.springframework.web.multipart.MultipartFile;

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
        String name = getFileNameByPath("/yefeng/20220920_200037.mp4");
        String name1 = getFileNameByPath(name);
        System.out.println("name: " + name);
        System.out.println("name1: " + name1);
    }

    public static String getFileNameByPath(MultipartFile file) {
        String fileName = file.getOriginalFilename();
//        String fileName1 = file.getResource().getFilename();


        String name = FileNameUtil.getFileNameByPath(fileName);
        return name;
    }
    public static String getMimeType(MultipartFile file){
        String name = getFileNameByPath(file);
        return FileUtil.getMimeType(name);
    }
}
