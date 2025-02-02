package com.yefeng.netdisk.front.controller;

import cn.hutool.crypto.digest.BCrypt;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xkcoding.justauth.AuthRequestFactory;
import com.yefeng.netdisk.common.exception.BizException;
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
import me.zhyd.oauth.model.AuthCallback;
import me.zhyd.oauth.model.AuthResponse;
import me.zhyd.oauth.request.AuthRequest;
import me.zhyd.oauth.utils.AuthStateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
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
    public ApiResult<String> login(@PathVariable String type, HttpServletResponse response) {
        AuthRequest authRequest = null;
        try {
            authRequest = factory.get(type);
        } catch (Exception e) {
            throw new BizException("暂不支持该第三方登录");
        }
        String authorize = authRequest.authorize(AuthStateUtils.createState());
        log.info("response={}", authorize);
        return ResultUtil.success(authorize);
//        response.sendRedirect(authorize);
    }

    @Resource
    UserServiceImpl userService;

    @GetMapping("/{type}/callback")
    public void callback(@PathVariable String type, AuthCallback callback, HttpServletResponse hresponse) {

        AuthRequest authRequest = factory.get(type);
        AuthResponse response = null;
        try {
            response = authRequest.login(callback);
        } catch (Exception e) {
            log.error("获取授权失败，",e);
            throw new RuntimeException("网络异常，请稍后再试");
        }

        log.info("【response】= {}", JSONUtil.toJsonStr(response));

        JSONObject data = JSONUtil.parseObj(response.getData());
        log.info("【data】= {}", data);
        if (response.getCode() == 2000) {
            User user = getUserOrRegister(response, type);
            if (user != null) {
                UserVo userVo = UserMapperStruct.INSTANCE.toDto(user);
                String userToken = CreateUserToken(user);
                userVo.setToken(userToken);
                writeHtml(userVo, hresponse);
            }else{
                log.error("用户通过第三方登录，失败");
                throw new BizException("业务发送异常，请等待修复");
            }
        } else {
            throw new RuntimeException("授权失败：请稍后再试，授权码是：" + response.getCode());
        }
    }


    private void writeHtml(UserVo userVo, HttpServletResponse httpServletResponse) {
        StringBuilder stringBuilder = new StringBuilder();
//       String html = "<!DOCTYPE html>\n" +
        String html = "<html lang=\"en\">\n" +
                "  <head>\n" +
                "    <meta charset=\"UTF-8\" />\n" +
                "    <meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\" />\n" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\" />\n" +
                "    <title>Netdisk登录页</title>\n" +
                "    <style>\n" +
                "      .loadingSeven {\n" +
                "        position: fixed;\n" +
                "        width: 160px;\n" +
                "        height: 60px;\n" +
                "        left: calc(50% - 40px);\n" +
                "        top: calc(50% - 40px);\n" +
                "      }\n" +
                "      .loadingSeven span {\n" +
                "        display: inline-block;\n" +
                "        width: 8px;\n" +
                "        height: 100%;\n" +
                "        border-radius: 4px;\n" +
                "        background: lightgreen;\n" +
                "        animation: loadsaven 1.04s ease infinite;\n" +
                "      }\n" +
                "      @keyframes loadsaven {\n" +
                "        0%,\n" +
                "        100% {\n" +
                "          height: 40px;\n" +
                "          background: lightgreen;\n" +
                "        }\n" +
                "        50% {\n" +
                "          height: 60px;\n" +
                "          margin-top: -20px;\n" +
                "          background: lightblue;\n" +
                "        }\n" +
                "      }\n" +
                "      .loadingSeven span:nth-child(2) {\n" +
                "        animation-delay: 0.13s;\n" +
                "      }\n" +
                "      .loadingSeven span:nth-child(3) {\n" +
                "        animation-delay: 0.26s;\n" +
                "      }\n" +
                "      .loadingSeven span:nth-child(4) {\n" +
                "        animation-delay: 0.39s;\n" +
                "      }\n" +
                "      .loadingSeven span:nth-child(5) {\n" +
                "        animation-delay: 0.52s;\n" +
                "      }\n" +
                "    </style>\n" +
                "  </head>\n";
        stringBuilder.append(html);
        html = "  <body>\n" +
                "    <div class=\"loadingSeven\" style=\"text-align: center\">\n" +
                "      <span></span>\n" +
                "      <span></span>\n" +
                "      <span></span>\n" +
                "      <span></span>\n" +
                "      <span></span>\n" +
                "      <div>登录中，请稍后</div>\n" +
                "    </div>\n" +
                "\n" +
                "    <script>\n" +
                "      window.addEventListener(\"load\", () => {\n" +
                "        console.log(\"Welcome\");\n" +
                "        const message = '" + JSONUtil.toJsonStr(userVo) + "';\n" +
                "        window.opener.parent.postMessage(message, \"*\");\n" +
                "        window.parent.close();\n" +
                "      });\n" +
                "    </script>\n" +
                "  <script>\n" +
                "   const timer = setTimeout(() => {\n" +
                "    alert(\"登录超时/错误，请返回页面重新登陆！\");\n" +
                "    clearTimeout(timer);\n" +
                "    window.parent.close();\n" +
                "   }, 1 * 30 * 1000);\n" +
                "\n" +
                "   window.addEventListener(\"load\", () => {\n" +
                "    console.log(\"Welcome\");\n" +
                "    const message = result;\n" +
                "    window.opener.parent.postMessage(message, \"*\");\n" +
                "    clearTimeout(timer);\n" +
                "    window.parent.close();\n" +
                "   });\n" +
                "  </script>" +
                "  </body>\n" +
                "</html>\n";
        stringBuilder.append(html);
        httpServletResponse.setContentType("text/html;charset=utf-8");
        httpServletResponse.setCharacterEncoding("utf-8");
        try {
            httpServletResponse.getWriter().println(stringBuilder);
            return;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private User getUserOrRegister(AuthResponse response, String type) {
        JSONObject data = JSONUtil.parseObj(response.getData());
        log.info("【data】= {}", data);
        String uuid = data.getStr("uuid");
        UserThirdAuth one = userThirdAuthService.getOne(new QueryWrapper<UserThirdAuth>().eq("openid", uuid));
        if (one != null) {
            // 已经绑定过了
            return userService.getOne(new QueryWrapper<User>().eq("id", one.getUserId()));
        } else {
            // 没有绑定过,需要绑定
            String username = data.getStr("username");
            String avatar = data.getStr("avatar");
            String email = data.getStr("email");
            String gender = data.getStr("gender");
            String token = data.getJSONObject("token").getStr("accessToken");
            User user = new User();
            user.setUsername(username);
            user.setImgPath(avatar);
            user.setEmail(email);
            user.setSalt(BCrypt.gensalt());//获取盐

            UserThirdAuth thirdAuth = new UserThirdAuth();
            thirdAuth.setAccessToken(token);
            thirdAuth.setOpenid(uuid);
            thirdAuth.setLoginType(type);

            boolean flag = userThirdAuthService.registerOauthUser(user, thirdAuth);
            if (flag) return user;
        }
        return null;
    }
}
