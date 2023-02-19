package com.yefeng.netdisk.front.util;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class is for
 *
 * @author 夜枫
 * @version 2023-02-11 14:02
 */
public class QueryWrapperUtil<T> {
    public static <T> QueryWrapper<T> getWrapper(String queryString) {
        QueryWrapper<T> wrapper = new QueryWrapper<>();
        if ("".equals(queryString)) {
            return wrapper;
        }

//        String queryString = "parent_file_id = 621c5ea65a1b6c367517412cbf90645fa3d41c22 and name = 笔试.mp4";
//        String queryString1 = "parent_file_id match 621c5ea65a1b6c367517412cbf90645fa3d41c22 or name = 笔试.mp4";

        String pattern1 = "(or)|(and)";
        String pattern2 = "=|(!=)|>=?|<=?|(match)";

        Pattern p1 = Pattern.compile(pattern1);
        Pattern p2 = Pattern.compile(pattern2);

        Matcher matcher1 = p1.matcher(queryString);

        if (matcher1.find()) {
            String andOr = matcher1.group();
            String[] andOrString = queryString.split(pattern1);

            if (andOr.equals("and")) {
                wrapper.and(w -> {
                    getWrapper(w, andOrString[0]);
                    getWrapper(w, andOrString[1]);
                });
            } else if (andOr.equals("or")) {
                wrapper.or(w -> {
                    getWrapper(w, andOrString[0]);
                    getWrapper(w, andOrString[1]);
                });
            }
        } else {
            getWrapper(wrapper, queryString);
        }
//        System.out.println(wrapper.getSqlSegment());
        return wrapper;
    }


    public static <T> void getWrapper(QueryWrapper<T> wrapper, String input) {
        String pattern2 = "=|(!=)|>=?|<=?|(match)";
        Pattern p2 = Pattern.compile(pattern2);
        Matcher matcher = p2.matcher(input.trim());
        if (matcher.find()) {
            String group = matcher.group();
            String[] split = input.split(pattern2);
            split[1] = split[1].trim();
            split[0] = split[0].trim();
            switch (group) {
                case "=":
                    wrapper.eq(split[0], split[1]);
                    break;
                case "match":
                    wrapper.like(split[0], split[1]);
                    break;
                case "!=":
                    wrapper.ne(split[0], split[1]);
                    break;
                case ">":
                    wrapper.gt(split[0], split[1]);
                    break;
                case ">=":
                    wrapper.ge(split[0], split[1]);
                    break;
                case "<":
                    wrapper.lt(split[0], split[1]);
                    break;
                case "<=":
                    wrapper.le(split[0], split[1]);
                    break;
                default:
                    break;
            }
        }
    }

    public static <T> QueryWrapper<T> getWrapper(String queryString, Class<T> className) {
        QueryWrapper<T> wrapper = new QueryWrapper<>();
        if ("".equals(queryString)) {
            return wrapper;
        }

//        String queryString = "parent_file_id = 621c5ea65a1b6c367517412cbf90645fa3d41c22 and name = 笔试.mp4";
//        String queryString1 = "parent_file_id match 621c5ea65a1b6c367517412cbf90645fa3d41c22 or name = 笔试.mp4";

        String pattern1 = "(or)|(and)";
        String pattern2 = "=|(!=)|>=?|<=?|(match)";

        Pattern p1 = Pattern.compile(pattern1);
        Pattern p2 = Pattern.compile(pattern2);

        Matcher matcher1 = p1.matcher(queryString);

        if (matcher1.find()) {
            String andOr = matcher1.group();
            String[] andOrString = queryString.split(pattern1);

            if ("and".equals(andOr)) {
                wrapper.and(w -> {
                    getWrapper(w, andOrString[0]);
                    getWrapper(w, andOrString[1]);
                });
            } else if ("or".equals(andOr)) {
                wrapper.or(w -> {
                    getWrapper(w, andOrString[0]);
                    getWrapper(w, andOrString[1]);
                });
            }
        } else {
            getWrapper(wrapper, queryString);
        }
//        System.out.println(wrapper.getSqlSegment());
        return wrapper;
    }
}
