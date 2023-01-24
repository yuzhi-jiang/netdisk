package com.yefeng.netdisk.common.util;

import org.springframework.util.AntPathMatcher;

/**
 * This class is for
 *
 * @author 夜枫
 * @version 2023-01-24 18:27
 */
public class PathUtil {
    private static final AntPathMatcher MATCHER = new AntPathMatcher();


    /**
     * 通配符匹配路径
     * 如：wildcardMatch(/api/*,/api/yourBatman/address)=false
     * wildcardMatch(/api/*,/api/yourBatmanOther)=true
     * @param pattern  匹配pattern
     * @param path  请求路径
     * @return 匹配结果
     */
    public static boolean wildcardMatch(String pattern, String path){
        return MATCHER.match(pattern, path);
    }


}
