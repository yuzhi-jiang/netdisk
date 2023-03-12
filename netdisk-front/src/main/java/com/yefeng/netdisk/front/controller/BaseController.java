package com.yefeng.netdisk.front.controller;

import com.yefeng.netdisk.common.exception.BizException;
import com.yefeng.netdisk.common.result.ApiResult;
import com.yefeng.netdisk.common.result.ResultUtil;
import com.yefeng.netdisk.common.util.JWTUtil;
import com.yefeng.netdisk.common.validator.Assert;
import com.yefeng.netdisk.front.entity.User;

import java.util.HashMap;

/**
 * This class is for
 *
 * @author 夜枫
 * @version 2023-03-11 17:41
 */
public class BaseController {


    public String CreateUserToken(User user) {
        Assert.isNull(user, "用户不存在");
        return JWTUtil.createToken(user.getId(), new HashMap<>() {
            {
                put("username", user.getUsername());
                if (user.getEmail()!=null) {
                    put("email", user.getEmail());
                }

                if (user.getMobile()!=null) {
                    put("mobile", user.getMobile());
                }
            }
        });
    }

    protected ApiResult getResultByFlag(Boolean flag) {
        try {
            if (flag) {
                return ResultUtil.success();
            } else {
                return ResultUtil.fail();
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new BizException(e.getMessage());
        }
    }
}
