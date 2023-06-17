package com.yefeng.netdisk.common.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileNameSorter {
    public static void main(String[] args) {
        String[] fileNames = {"模板7(2).txt.dox", "模板7(1).txt.dox","模板7.txt"};
        List<String> sortedFileNames = sortFileNames(Arrays.asList(fileNames));
        System.out.println(sortedFileNames);
    }

    public static List<String> sortFileNames(List<String> fileNames) {
        List<String> fileNameList = fileNames == null ? new ArrayList<>() : fileNames;
        fileNameList.sort((fileName1, fileName2) -> {
            String fileNamePattern = "(.*?)(\\((\\d+)\\))?(\\..+)?$";
            Pattern pattern = Pattern.compile(fileNamePattern);

            Matcher matcher1 = pattern.matcher(fileName1);
            Matcher matcher2 = pattern.matcher(fileName2);
            if (matcher1.matches() && matcher2.matches()) {
                String name1 = matcher1.group(1);
                String name2 = matcher2.group(1);
                int result = name1.compareTo(name2);
                if (result != 0) {
                    return result;
                }

                String index1 = matcher1.group(3);
                String index2 = matcher2.group(3);
                if (index1 == null && index2 == null) {
                    return 0;
                } else if (index1 == null) {
                    return -1;
                } else if (index2 == null) {
                    return 1;
                } else {
                    return Integer.compare(Integer.parseInt(index1), Integer.parseInt(index2));
                }
            } else {
                return fileName1.compareTo(fileName2);
            }
        });

        return fileNameList;
    }
}