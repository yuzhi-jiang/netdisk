package com.yefeng.netdisk.front.controller;

import cn.hutool.crypto.digest.BCrypt;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xkcoding.justauth.AuthRequestFactory;
import com.yefeng.netdisk.common.result.ApiResult;
import com.yefeng.netdisk.common.result.ResultUtil;
import com.yefeng.netdisk.front.entity.User;
import com.yefeng.netdisk.front.entity.UserThirdAuth;
import com.yefeng.netdisk.front.mapStruct.mapper.UserMapperStruct;
import com.yefeng.netdisk.front.service.IUserThirdAuthService;
import com.yefeng.netdisk.front.service.impl.UserServiceImpl;
import com.yefeng.netdisk.front.vo.UserVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.zhyd.oauth.config.AuthConfig;
import me.zhyd.oauth.model.AuthCallback;
import me.zhyd.oauth.model.AuthResponse;
import me.zhyd.oauth.request.AuthGithubRequest;
import me.zhyd.oauth.request.AuthRequest;
import me.zhyd.oauth.utils.AuthStateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author yefeng
 * @since 2023-01-15
 */
@Slf4j
@RestController
@RequestMapping("/oauth")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserThirdAuthController extends BaseController {

    @Resource
    IUserThirdAuthService userThirdAuthService;

    private final AuthRequestFactory factory;

    @GetMapping
    public List<String> list() {
        return factory.oauthList();
    }

    @GetMapping("/login/{type}")
    public void login(@PathVariable String type, HttpServletResponse response) throws IOException {
        AuthRequest authRequest = factory.get(type);
        String authorize = authRequest.authorize(AuthStateUtils.createState());
        log.info("response={}", authorize);
        response.sendRedirect(authorize);
    }

    @Resource
    UserServiceImpl userService;

    @RequestMapping("/{type}/callback")
    public ApiResult<UserVo> login(@PathVariable String type, AuthCallback callback) {
        AuthRequest authRequest = factory.get(type);
        AuthResponse response = authRequest.login(callback);
        log.info("【response】= {}", JSONUtil.toJsonStr(response));

        JSONObject data = JSONUtil.parseObj(response.getData());
        log.info("【data】= {}", data);
        if (data.getInt("code") == 2000) {
            String uuid = data.getStr("uuid");
            UserThirdAuth one = userThirdAuthService.getOne(new QueryWrapper<UserThirdAuth>().eq("openid", uuid));
            if (one != null) {
                // 已经绑定过了
                User user = userService.getOne(new QueryWrapper<User>().eq("id", one.getUserId()));

                UserVo userVo = UserMapperStruct.INSTANCE.toDto(user);
                String token = CreateUserToken(user);
                userVo.setToken(token);
                return ResultUtil.success(userVo);
            } else {
                // 没有绑定过,需要绑定
                String username = data.getStr("username");
                String avatar = data.getStr("avatar");
                String email = data.getStr("email");
                String gender = data.getStr("gender");
                String token = data.getJSONObject("token").getStr("access_token");
                User user = new User();
                user.setUsername(username);
                user.setImgPath(avatar);
                user.setEmail(email);
                user.setSalt(BCrypt.gensalt());//获取盐

                UserThirdAuth thirdAuth = new UserThirdAuth();
                thirdAuth.setAccessToken(token);
                thirdAuth.setOpenid(uuid);
                thirdAuth.setLoginType(type);


                boolean flag = userService.registerUserAndInitDisk(user);
                if (flag) {
                    thirdAuth.setUserId(user.getId());
                    boolean save = userThirdAuthService.save(thirdAuth);
                    if (save) {
                        UserVo userVo = UserMapperStruct.INSTANCE.toDto(user);
                        String userToken = CreateUserToken(user);
                        userVo.setToken(userToken);
                        return ResultUtil.success(userVo);
                    }
                }
                throw new RuntimeException("授权失败,请重试");

            }
        }
        throw new RuntimeException("授权失败,请重试");
    }

//    @RequestMapping("/render")
//    public void renderAuth(HttpServletResponse response) throws IOException {
//        AuthRequest authRequest = getAuthRequest();
//        String state = AuthStateUtils.createState();
//        String authorize = authRequest.authorize(state);
//        response.sendRedirect(authorize);
//    }

//    @RequestMapping("/callback")
//    public Object login(AuthCallback callback) {
//        AuthRequest authRequest = getAuthRequest();
//        AuthResponse response = authRequest.login(callback);
//
//        return response;
//    }
//
//    private AuthRequest getAuthRequest() {
//        return new AuthGithubRequest(AuthConfig.builder()
//                .clientId("a4bad19c0484ac324762")
//                .clientSecret("fa016f426e037f944549b86a13576a0ed036f555")
//                .redirectUri("http://127.0.0.1:8082/user/fort/oauth/callback/github")
//                .build());
//    }
    private User getOrDefault(User user,UserThirdAuth thirdAuth) {
        if (user == null) {
            return new User();
        }
        return user;
    }


}
