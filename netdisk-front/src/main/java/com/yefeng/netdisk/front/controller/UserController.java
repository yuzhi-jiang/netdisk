package com.yefeng.netdisk.front.controller;


import cn.hutool.crypto.digest.BCrypt;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.yefeng.netdisk.common.constans.LoginEnum;
import com.yefeng.netdisk.common.exception.BizException;
import com.yefeng.netdisk.common.result.ApiResult;
import com.yefeng.netdisk.common.result.HttpCodeEnum;
import com.yefeng.netdisk.common.result.ResultUtil;
import com.yefeng.netdisk.common.util.CheckUtil;
import com.yefeng.netdisk.common.util.JWTUtil;
import com.yefeng.netdisk.common.validator.Assert;
import com.yefeng.netdisk.common.validator.ValidatorUtils;
import com.yefeng.netdisk.front.bo.LoginBo;
import com.yefeng.netdisk.front.bo.RegisterBo;
import com.yefeng.netdisk.front.bo.ResetPasswordBo;
import com.yefeng.netdisk.front.dto.BUser;
import com.yefeng.netdisk.front.entity.User;
import com.yefeng.netdisk.front.mapStruct.mapper.UserMapperStruct;
import com.yefeng.netdisk.front.service.IDiskService;
import com.yefeng.netdisk.front.service.impl.UserServiceImpl;
import com.yefeng.netdisk.front.task.SendEmailByRegisterTask;
import com.yefeng.netdisk.front.util.*;
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
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.ExecutorService;


/**
 * 用户模块
 *
 * @author yefeng
 * @since 2023-01-15
 */

@Api(tags = "用户模块")
@Slf4j
@RestController
@RequestMapping("/user")
@CrossOrigin
public class UserController extends BaseController {

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
    @Resource(name = "commonQueueThreadPool")
    ExecutorService commonQueueThreadPool;


    @Resource
    HttpServletResponse response;


    @ApiOperation(value = "用户登录,使用用户名/邮箱，或是手机号")
    @PostMapping("/login")
    public ApiResult<UserVo> login(@RequestBody LoginBo loginBo,Integer type) {




        //用户/邮箱密码
        if (type == LoginEnum.ACCOUNT.getCode() || type == LoginEnum.MOBILE_PASS.getCode()) {

            String account = loginBo.getAccount();
            QueryWrapper<User> query = new QueryWrapper<User>();

            if (type == LoginEnum.MOBILE_PASS.getCode()) {
                account = loginBo.getAccount();
                query.eq("mobile", account);
            } else {
                query.eq("username", account).or().eq("email", account);
            }

            User user = userService.getOne(query);

            Assert.isNull(user, "用户不存在");

            String password = loginBo.getPassword();
            String password1 = user.getPassword();
            log.info("加密密文为：{}", password1);
            if (!BCrypt.checkpw(password, user.getPassword())) {
                throw new BizException("用户账号或密码错误！");
            }


            UserVo userVo = UserMapperStruct.INSTANCE.toDto(user);


            String token = CreateUserToken(user);

            userVo.setToken(token);
            //将userid设置到header中
//            response.setHeader("userid", user.getId().toString());
            return ResultUtil.success(userVo);
        }
        else if (type == LoginEnum.MOBILE_CAPTCHA.getCode()) {
            String mobile =loginBo.getMobile();
            String captcha =loginBo.getCaptcha();

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
                user.setStatus(UserStatusEnum.NORMAL);//手机号码登录直接就激活了
                boolean flag = userService.registerUserAndInitDisk(user);
                if (!flag) {
                    throw new BizException("注册失败");
                }
            }

            user = userService.getOne(query);
            Assert.isNull(user, "用户不存在");


            UserVo userVo =UserMapperStruct.INSTANCE.toDto(user);

            String token = CreateUserToken(user);
            userVo.setToken(token);
            return ResultUtil.success(userVo);
        }

        log.info("params is {}", loginBo);
        throw new BizException("登录方式不正确");
    }



    @Value("${mycloud.useractive.tokenExpire:7200}")
    Long userActiveTime;
    /**
     * 注册
     * @param registerBo
     * @return
     */

    @PostMapping("/register")
    public ApiResult<String> register(@RequestBody RegisterBo registerBo) {
        String type = registerBo.getType();
        Assert.isNull(type, "注册类型不能为空");

        // 1. 通过用户名称密码邮箱的表单
        if (Objects.equals(LoginEnum.ACCOUNT.getValue(), type)) {

            BUser buser = new BUser();

            buser.setEmail(registerBo.getEmail());
            buser.setPassword(registerBo.getPassword());
            buser.setUsername(registerBo.getUsername());

            ValidatorUtils.validateEntity(buser);

            User user = new User();
            BeanUtils.copyProperties(buser, user);

            user.setStatus(UserStatusEnum.DISABLE);
            user.setSalt(BCrypt.gensalt());//获取盐
            user.setPassword(BCrypt.hashpw(user.getPassword(), user.getSalt()));


            boolean flag = userService.registerUserAndInitDisk(user);
            if (flag) {

                //邮箱注册需要发送一个邮箱验
                commonQueueThreadPool.execute(new SendEmailByRegisterTask(user.getId(), user.getEmail(), userActiveTime));

                return new ApiResult(HttpCodeEnum.OK.getCode(), "注册成功,请激活邮箱");
            }
            return new ApiResult(HttpCodeEnum.FAIL.getCode(), "当前请求过大，请稍后再试");
        }
        // 2. 通过手机验证码注册
        else if (Objects.equals(LoginEnum.MOBILE_CAPTCHA.getValue(), type)) {

            String captcha = registerBo.getCaptcha();
            String mobile = registerBo.getMobile();
            CheckUtil.checkPhone(mobile);

            if(captchaUtil.checkCaptcha(mobile, captcha)){
                throw new BizException("验证码错误请重试！");
            }

            User user = new User();
            user.setMobile(mobile);
            user.setStatus(UserStatusEnum.NORMAL);
            user.setSalt(BCrypt.gensalt());//获取盐

            boolean flag = userService.registerUserAndInitDisk(user);
            if (flag) {

                return new ApiResult(HttpCodeEnum.OK.getCode(), "注册成功");
            }
            return new ApiResult(HttpCodeEnum.FAIL.getCode(), "当前请求过大，请稍后再试");

        }

        return new ApiResult(HttpCodeEnum.FAIL.getCode(), "请根据参数请求");
    }


    @Value("${webclient.url}")
    private String webClientUrl;

    //todo 激活

    @ApiOperation("激活用户")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId",value = "用户id",required = true),
            @ApiImplicitParam(name = "token" ,value = "token",required = true)
    })
    @GetMapping("/{userId}/active")
    public void active(@PathVariable String userId, @RequestParam("token") String token, HttpServletResponse response) throws IOException {


        JWTUtil.validateToken(token);
        Object[] payload = JWTUtil.getPayloadFromToken(token, "user_id", "email");
        String userId1 = (String) payload[0];
        String email = (String) payload[1];

        if(!userId.equals(userId1)){
            log.error("激活失败，用户id不匹配 userid={} ,token={}",userId,token);
            throw new BizException("激活失败，请重新激活");
        }

        UpdateWrapper<User> updateWrapper = new UpdateWrapper<User>().set("status", UserStatusEnum.NORMAL).eq("id", userId).eq("email", email);

        boolean flag = userService.update(updateWrapper);
        if(!flag){
            throw new BizException("激活失败，请重新激活");
        }



        //激活成功，跳转到登录页面
        response.sendRedirect(webClientUrl);
    }


    //绑定手机号码
    @ApiOperation("绑定手机号码")
    @PostMapping("/bindMobile")
    public ApiResult<String> bindMobile(@RequestParam("mobile") String mobile,
                                        @RequestParam("captcha") String captcha,
                                        @RequestParam("user_id") String userId) {

        CheckUtil.checkPhone(mobile);

        captchaUtil.checkCaptcha(mobile, captcha);

        UpdateWrapper<User> updateWrapper = new UpdateWrapper<User>().set("mobile", mobile).eq("id", userId);

        boolean flag = userService.update(updateWrapper);
        if(!flag){
            throw new BizException("绑定失败，请重新绑定");
        }
        return ResultUtil.success("绑定成功");
    }


    // 绑定第三方账号
//    @ApiOperation("绑定第三方账号")
//    @PostMapping("/bindThirdAccount")


    //注销用户
    @ApiOperation("注销用户")
    @PostMapping("/delete")
    public ApiResult<String> delete(@RequestHeader("user_id") String userId) {

        UpdateWrapper<User> updateWrapper = new UpdateWrapper<User>().set("status", UserStatusEnum.DISABLE).eq("id", userId);

        boolean flag = userService.update(updateWrapper);
        if(!flag){
            throw new BizException("注销失败，请重新注销");
        }
        return ResultUtil.success("注销成功");
    }

    //删除用户
    @ApiOperation("删除用户")
    @PostMapping("/remove")
    public ApiResult<String> remove(@RequestHeader("user_id") String userId) {

        boolean flag = userService.removeById(userId);
        if(!flag){
            throw new BizException("删除失败，请重新删除");
        }
        return ResultUtil.success("删除成功");
    }

    //解除绑定手机号码
    @ApiOperation("解除绑定手机号码")
    @PostMapping("/unbindMobile")
    public ApiResult<String> unbindMobile(@RequestParam("mobile") String mobile,
                                          @RequestParam("user_id") String userId) {

        CheckUtil.checkPhone(mobile);

        UpdateWrapper<User> updateWrapper = new UpdateWrapper<User>().set("mobile", null).eq("id", userId);

        boolean flag = userService.update(updateWrapper);
        if(!flag){
            throw new BizException("解除绑定失败，请重新解除绑定");
        }
        return ResultUtil.success("解除绑定成功");
    }

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
     * @param userId
     * @return
     */
    @ApiOperation("获取用户信息 调用这个接口需调用 userdisk接口获取用户的云盘信息")
    @GetMapping("/userinfo")
    public ApiResult<UserVo> getUser(@RequestHeader("user_id")@ApiParam("用户id") String userId) {
        Assert.isBlank(userId, "用户id不能为空");
        //todo 是否查看用户状态
        User user = userService.getById(Long.parseLong(userId));

        Assert.isNull(user, "获取用户信息失败，原因：用户不存在");

        return ResultUtil.success(UserMapperStruct.INSTANCE.toDto(user));
    }

    @ApiOperation("获取用户信息和云盘信息 与上面的接口不同的是，这个接口会返回用户的云盘信息")
    @GetMapping("/userdisk")
    public ApiResult<UserDiskVo> getUserDisk(@RequestHeader("user_id") String UserId) {
        //todo 是否查看用户状态
        User user = userService.getById(Long.parseLong(UserId));
        Assert.isNull(user, "用户不存在");


        UserVo userVo = UserMapperStruct.INSTANCE.toDto(user);

        DiskVo diskvo = diskService.getDiskInfoByUerId(user.getId());
        Assert.isNull(diskvo, "没有数据，用户已注销云盘");
        UserDiskVo userDiskVo = new UserDiskVo();
        userDiskVo.setUserVo(userVo);
        userDiskVo.setDiskVo(diskvo);


        return ResultUtil.success(userDiskVo);
    }


    //todo 修改用户信息 、密码，不包括头像
    @PutMapping("/userinfo")
    public ApiResult updateUserInfo(@RequestHeader("user_id") String userId, @RequestBody BUser bUser) {

        ValidatorUtils.validateEntity(bUser);
        User user = new User();
        user.setId(Long.valueOf(userId));
        BeanUtils.copyProperties(bUser, user);
        boolean flag = userService.updateById(user);
        if (flag) {
            return ResultUtil.success();
        }
        return ResultUtil.fail();
    }




    //根据用户的 username or email or mobile  获取用户信息
    @ApiOperation("根据用户的 username or email or mobile  获取用户信息,判断用户是否存在")
    @GetMapping("/exist")
    public UserVo existUser(String account) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<User>().eq("username", account).or().eq("email", account).or().eq("mobile", account);
        User user = userService.getOne(queryWrapper);
        if (user == null) {
            throw new BizException("用户不存在");
        }
        return UserMapperStruct.INSTANCE.toDto(user);
    }


    //在普通用户了绑定了电话的情况下可以用，否则是本来就是没有密码的
    // todo 忘记密码
    @ApiOperation("重置密码")
    @PostMapping("/resetPassword")
    public ApiResult forget(@RequestBody ResetPasswordBo params) {
        String account = params.getAccount();
        String captcha = params.getCaptcha();
        String password = params.getPassword();
        String confirmPassword = params.getConfirmPassword();

        if(!password.equals(confirmPassword)){
            throw new BizException("两次密码不一致");
        }

        //检查密码格式
        CheckUtil.checkPassword(password);


        //验证次数加一
        redisUtil.incr(RedisKeys.getForgetCapatchaKey(account), 1);

        //验证次数超过5次则冻结30分钟不准发验证码，冻结3次则今天不准/冻结用户

        if (!captchaUtil.checkCaptcha(account, captcha, true)) {
            throw new BizException("验证码错误请重试!");
        }
        User user = userService.getOne(new QueryWrapper<User>().eq("mobile", account).or().eq("email", account).or().eq("username", account));

        Assert.isNull(user, "用户不存在");

        user.setPassword(BCrypt.hashpw(password, user.getSalt()));

        //更新数据库
        Boolean flag = userService.updateById(user);

        // redis缓存
//        redisUtil.setObject(RedisKeys.getForgetCapatchaKey(mobile), mobile);
        return getResultByFlag(flag);
    }

    private User getUserByMobile(String mobile) {
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


}
