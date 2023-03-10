package com.yefeng.netdisk.front.controller;


import cn.hutool.captcha.ShearCaptcha;
import cn.hutool.crypto.digest.BCrypt;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yefeng.netdisk.common.constans.LoginEnum;
import com.yefeng.netdisk.common.exception.BizException;
import com.yefeng.netdisk.common.request.RequestParams;
import com.yefeng.netdisk.common.result.ApiResult;
import com.yefeng.netdisk.common.result.HttpCodeEnum;
import com.yefeng.netdisk.common.result.ResultUtil;
import com.yefeng.netdisk.common.util.CheckUtil;
import com.yefeng.netdisk.common.util.JWTUtil;
import com.yefeng.netdisk.common.validator.Assert;
import com.yefeng.netdisk.common.validator.ValidatorUtils;
import com.yefeng.netdisk.front.dto.BUser;
import com.yefeng.netdisk.front.entity.User;
import com.yefeng.netdisk.front.service.IDiskService;
import com.yefeng.netdisk.front.service.impl.UserServiceImpl;
import com.yefeng.netdisk.front.util.CaptchaUtil;
import com.yefeng.netdisk.front.util.RedisKeys;
import com.yefeng.netdisk.front.util.RedisUtil;
import com.yefeng.netdisk.front.util.SendUtils;
import com.yefeng.netdisk.front.vo.DiskVo;
import com.yefeng.netdisk.front.vo.UserDiskVo;
import com.yefeng.netdisk.front.vo.UserVo;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;


/**
 * <p>
 * 用户控制器
 * </p>
 *
 * @author yefeng
 * @since 2023-01-15
 */
@Slf4j
@RestController
@RequestMapping("/user")
@Api(tags = "用户")
public class UserController {


    @Resource
    UserServiceImpl userService;


    @Resource
    IDiskService diskService;

    @Resource
    CaptchaUtil captchaUtil;


    @Value("${mycloud.register.registerByMobileLogin}")
    boolean registerByMobileLogin;
    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private SendUtils sendUtils;

    @ApiOperation("获取邮箱验证码,,")
    @GetMapping("/captcha")
    public ApiResult getEmailcaptcha(@RequestParam("email") String email, @RequestParam(value = "isForgetType", required = false) Boolean isForgetType) {
        CheckUtil.checkEmail(email);

        if (isForgetType != null && isForgetType) {
            User user = userService.getOne(new QueryWrapper<User>().eq("email", email));
            Assert.isNull(user, "用户不存在，请先注册！");
        }

        String captcha = captchaUtil.createCaptcha(email);

        // todo 异步发 验证码
        log.info("the captcha:{}  is {}", email, captcha);

        sendUtils.send(email,captcha);

        //直接返回
        return ResultUtil.success();
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


    @ApiOperation(value = "用户登录,使用用户名/邮箱，或是手机号")
    @PostMapping("/login")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "type",value = "登陆类型,"),
            @ApiImplicitParam(name = "account",value = "用户"),
            @ApiImplicitParam(name = "mobile",value = "电话"),
            @ApiImplicitParam(name = "password",value = "密码"),
            @ApiImplicitParam(name = "captcha",value = "验证码")
    })
    public ApiResult login(@RequestBody RequestParams params) {

        int type = params.getIntValue("type");


        //用户/邮箱密码
        if (type == LoginEnum.ACCOUNT.getType() || type == LoginEnum.MOBILE_PASS.getType()) {

            String account = (String) params.get("account");
            QueryWrapper<User> query = new QueryWrapper<User>();

            if (type == LoginEnum.MOBILE_PASS.getType()) {
                account = (String) params.get("mobile");
                query.eq("mobile", account);
            } else {
                query.eq("username", account).or().eq("email", account);
            }

            User user = userService.getOne(query);

            Assert.isNull(user, "用户不存在");

            String password = (String) params.get("password");
            String password1 = user.getPassword();
            log.info("加密密文为：{}", password1);
            if (!BCrypt.checkpw(password, user.getPassword())) {
                throw new BizException("用户账号或密码错误！");
            }


            UserVo userVo = new UserVo();
            BeanUtils.copyProperties(user, userVo);
            String token = JWTUtil.createToken(userVo.getId());

            userVo.setToken(token);
            return ResultUtil.success(userVo);
        }
        else if (type == LoginEnum.MOBILE_CAPTCHA.getType()) {
            String mobile = (String) params.get("mobile");
            String captcha = (String) params.get("captcha");

            // todo 检查 验证码
            if (!captchaUtil.checkCaptcha(mobile, captcha)) {
                throw new BizException("验证码错误请重试！");
            }

            QueryWrapper<User> query = new QueryWrapper<>();

            query.eq("mobile", mobile);
            User user = userService.getOne(query);

            if (user == null && registerByMobileLogin) {
                //自动注册
                user = new User();
                user.setMobile(mobile);
                user.setSalt(BCrypt.gensalt());//获取盐
                userService.save(user);
                diskService.initDisk(user.getId());
            }

            user = userService.getOne(query);
            Assert.isNull(user, "用户不存在");


            UserVo userVo = new UserVo();
            BeanUtils.copyProperties(user, userVo);

            String token = JWTUtil.createToken(userVo.getId());

            userVo.setToken(token);
            return ResultUtil.success(userVo);
        }

        log.info("params is {}", params.toString());
        throw new BizException("登录方式不正确");
    }

    /**
     * 注册
     * @param params
     * @return
     */
    @PostMapping("/register")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "type",value = "注册类型,类型枚举"),
            @ApiImplicitParam(name = "email",value = "邮箱"),
            @ApiImplicitParam(name = "mobile",value = "电话",required = false),
            @ApiImplicitParam(name = "password",value = "密码"),
            @ApiImplicitParam(name = "captcha",value = "验证码"),
            @ApiImplicitParam(name = "username",value = "用户名")
    })
    public ApiResult register(@RequestBody RequestParams params) {
        Integer type = params.getIntValue("type");
        Assert.isNull(type, "注册类型不能为空");

        // 1. 通过用户名称密码邮箱的表单
        if (LoginEnum.ACCOUNT.getType() == type) {

            BUser buser = new BUser();

            buser.setEmail(params.getStringValue("email"));
            buser.setPassword(params.getStringValue("password"));
            buser.setUsername(params.getStringValue("username"));


            ValidatorUtils.validateEntity(buser);

            User user = new User();
            BeanUtils.copyProperties(buser, user);
            user.setSalt(BCrypt.gensalt());//获取盐
            user.setPassword(BCrypt.hashpw(user.getPassword(), user.getSalt()));

            boolean flag = userService.save(user);
            if (flag) {
                diskService.initDisk(user.getId());
                return new ApiResult(HttpCodeEnum.OK.getCode(), "注册成功");
            }
            return new ApiResult(HttpCodeEnum.FAIL.getCode(), "当前请求过大，请稍后再试");
        }
        // 2. 通过手机验证码注册
        else if (LoginEnum.MOBILE_CAPTCHA.getType() == type) {

            String captcha = params.getStringValue("captcha");
            String mobile = params.getStringValue("mobile");
            CheckUtil.checkPhone(mobile);

            captchaUtil.checkCaptcha(mobile, captcha);

            User user = new User();
            user.setMobile(mobile);

            user.setSalt(BCrypt.gensalt());//获取盐

            boolean flag = userService.save(user);
            if (flag) {
                diskService.initDisk(user.getId());
                return new ApiResult(HttpCodeEnum.OK.getCode(), "注册成功");
            }
            return new ApiResult(HttpCodeEnum.FAIL.getCode(), "当前请求过大，请稍后再试");

        }

        return new ApiResult(HttpCodeEnum.FAIL.getCode(), "请根据参数请求");
    }

    /**
     * 校验验证码
     * @param code
     * @param request
     * @return
     */
    @ApiOperation("验证码校验")
    @GetMapping("checkcaptcha")
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


    //todo 注册


    //todo 退出

    /**
     * 退出
     * @param token
     * @return
     */
    @ApiOperation("用户登出")
    @GetMapping("/logout")
    public ApiResult logout(@RequestHeader("Authorization")@ApiParam("token 从header中获取") String token) {
        redisUtil.delete(token);
        return ResultUtil.success();
    }


    /**
     * 获取用户信息
     * @param subject
     * @return
     */
    @ApiOperation("获取用户信息")
    @GetMapping("/userinfo")
    public ApiResult getUser(@RequestHeader("subject")@ApiParam("用户id") String subject) {
        //todo 是否查看用户状态
        User user = userService.getById(Long.parseLong(subject));
        Assert.isNull(user, "用户不存在");


        UserVo userVo = new UserVo();

        BeanUtils.copyProperties(user, userVo);

        return ResultUtil.success(userVo);
    }

    @GetMapping("/userDisk")
    public ApiResult getUserDisk(@RequestHeader("subject") String subject) {
        //todo 是否查看用户状态
        User user = userService.getById(Long.parseLong(subject));
        Assert.isNull(user, "用户不存在");


        UserVo userVo = new UserVo();

        BeanUtils.copyProperties(user, userVo);

        DiskVo diskvo = diskService.getDiskInfoByUerId(user.getId());
        Assert.isNull(diskvo, "没有数据，用户已注销云盘");
        UserDiskVo userDiskVo = new UserDiskVo();
        userDiskVo.setUserVo(userVo);
        userDiskVo.setDiskVo(diskvo);


        return ResultUtil.success(userDiskVo);
    }


    //todo 修改用户信息 、密码，不包括头像
    @PutMapping("/userinfo")
    public ApiResult updateUserInfo(@RequestHeader("subject") String subject, @RequestBody BUser bUser) {

        ValidatorUtils.validateEntity(bUser);
        User user = new User();
        user.setId(Long.valueOf(subject));
        BeanUtils.copyProperties(bUser, user);
        boolean flag = userService.updateById(user);
        if (flag) {
            return ResultUtil.success();
        }
        return ResultUtil.fail();

    }

    //todo 修改/上传头像


    //在普通用户了绑定了电话的情况下可以用，否则是本来就是没有密码的
    // todo 忘记密码
    @ApiOperation("忘记密码")
    @PostMapping("/forget")
    public ApiResult forget(@RequestBody RequestParams params) {
        String mobile = params.getStringValue("mobile");
        String captcha = params.getStringValue("captcha");
        String password = params.getStringValue("password");

        //检查密码格式
        CheckUtil.checkPassword(password);

        //验证次数加一
        redisUtil.incr(RedisKeys.getForgetCapatchaKey(mobile), 1);

        //验证次数超过5次则冻结30分钟不准发验证码，冻结3次则今天不准/冻结用户

        if (!captchaUtil.checkCaptcha(mobile, captcha, true)) {
            throw new BizException("验证码错误请重试!");
        }
        User user = getUserByMobile(mobile);


        user.setPassword(BCrypt.hashpw(password, user.getSalt()));

        //更新数据库
        Boolean flag = userService.updateById(user);

        // redis缓存
//        redisUtil.setObject(RedisKeys.getForgetCapatchaKey(mobile), mobile);
        return getResultByFlag(flag);
    }

    public User getUserByMobile(String mobile) {
        return userService.getOne(new QueryWrapper<User>().eq("mobile", mobile));
    }


    /**
     * 上传头像
     * @param avatar
     * @param userId
     * @return
     */
    @RequestMapping(value = "/avatar", method = RequestMethod.POST)
    public ApiResult uploadAvatar(@RequestParam("avatar") MultipartFile avatar, @RequestParam("userId") String userId) {
        if (avatar == null || avatar.isEmpty()) {
            return ResultUtil.failMsg("上传头像失败，请重新上传");
        }
        String originalFilename = avatar.getOriginalFilename();
        // 根据原始文件名获取文件后缀
        String suffix = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
        // 如果文件后缀不符合要求，则返回失败信息
        if (!suffix.equals("jpg") && !suffix.equals("png")) {
            return ResultUtil.failMsg("上传头像失败，仅支持jpg或png格式的图片");
        }
        // 上传头像的文件夹
//        String folder = "avatar/" + userId;

        Boolean flag = userService.uploadAvatar(avatar, userId);

        return getResultByFlag(flag);

    }


    public ApiResult getResultByFlag(Boolean flag) {
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
