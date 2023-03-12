package com.yefeng.netdisk.front.controller;

import cn.hutool.captcha.ShearCaptcha;
import com.yefeng.netdisk.common.result.ApiResult;
import com.yefeng.netdisk.common.result.HttpCodeEnum;
import com.yefeng.netdisk.common.result.ResultUtil;
import com.yefeng.netdisk.common.util.CheckUtil;
import com.yefeng.netdisk.front.util.CaptchaUtil;
import com.yefeng.netdisk.front.util.RedisUtil;
import com.yefeng.netdisk.front.util.SendUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 *
 *
 * 验证码
 * @author 夜枫
 * @version 2023-03-11 17:32
 */
@Api(tags = "验证码接口")
@RestController
@Slf4j
@RequestMapping("/captcha")
public class CaptchaController {
    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private SendUtils sendUtils;


    @Resource
    CaptchaUtil captchaUtil;
    /**
     * 校验验证码
     * @param code
     * @param request
     * @return
     */
    @ApiOperation("验证码校验")
    @GetMapping("/check")
    ApiResult checkImageCaptcha(@RequestParam("code") String code, HttpServletRequest request) {
//        String captchaCode =(String) request.getAttribute("captchaCode");

        HttpSession session = request.getSession();
        String captchaCode = session.getAttribute("captchaCode").toString();

        log.info("captchaCode:{},,code:{}", captchaCode, code);

        if (code.equals(captchaCode)) {
            return ResultUtil.success();
        }
        return new ApiResult(HttpCodeEnum.FAIL.getCode(), "验证码不匹配");
    }

    //    @GetMapping("/imageCaptcha")
    //得解决分布式下解决方式 项目采用无状态，不能用redis sessions
    public void getImageCaptcha(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {


        httpServletResponse.setHeader("Pragma", "no-cache");
        httpServletResponse.setHeader("Cache-Control", "no-cache");
        httpServletResponse.setDateHeader("Expires", 0);
        httpServletResponse.setContentType("image/jpeg");


        ShearCaptcha captcha = cn.hutool.captcha.CaptchaUtil.createShearCaptcha(200, 100, 4, 4);

        String code = captcha.getCode();


//        httpServletRequest.setAttribute("captchaCode", code);


        HttpSession session = httpServletRequest.getSession();
        session.removeAttribute("captchaCode");
        session.setAttribute("captchaCode", code);

        try {
            ServletOutputStream outputStream = httpServletResponse.getOutputStream();
            captcha.write(outputStream);
            outputStream.close();
        } catch (IOException e) {
            log.info("io异常 exception:{}", e.getMessage());
        }

    }


    @ApiOperation("获取手机验证码,还未实现手机验证码发生,但提供了测试接口")
    @GetMapping("/mobile")
    public ApiResult getcaptcha(@RequestParam("mobile") String mobile) {
        CheckUtil.checkPhone(mobile);


        String captcha = captchaUtil.createCaptcha(mobile);

        // todo 异步发 验证码
        log.info("the mobile:{} captcha is {}", mobile, captcha);

        sendUtils.sendMobile(mobile,captcha);

        //直接返回
        return ResultUtil.success();//此次为了测试直接返回
    }


    @ApiOperation("获取邮箱验证码,,")
    @GetMapping("/email")
    public ApiResult getEmailcaptcha(@RequestParam("email") String email) {
        CheckUtil.checkEmail(email);

        String captcha = captchaUtil.createCaptcha(email);

        // todo 异步发 验证码
        log.info("the captcha:{}  is {}", email, captcha);

        sendUtils.sendEmail(email,captcha);

        //直接返回
        return ResultUtil.success();
    }

}
