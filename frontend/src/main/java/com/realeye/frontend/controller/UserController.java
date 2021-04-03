package com.realeye.frontend.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.realeye.frontend.entity.APIKey;
import com.realeye.frontend.entity.User;
import com.realeye.frontend.service.UserService;
import com.realeye.frontend.service.APIKeyService;
import com.realeye.frontend.utils.ResultBody;
import com.realeye.frontend.utils.jwt.JWTToken;
import com.realeye.frontend.utils.jwt.JwtAuthenticatioToken;
import com.realeye.frontend.utils.jwt.annotation.JwtTokenInit;
import com.realeye.frontend.utils.jwt.utils.SecurityUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.util.Date;

@RestController
@RequestMapping("/user")
@Api(tags = "用户登陆 详情相关")
@Validated
public class UserController {

    @Resource
    private AuthenticationManager authenticationManager;

    @Resource
    private UserService userService;

    @Resource
    private APIKeyService apiKeyService;

    @GetMapping("/register")
    public ResultBody register(@NotNull String username, @NotNull String password, HttpServletRequest request) {

        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("username", username);
        User one = userService.getOne(wrapper);
        if (one == null) {
            return ResultBody.newErrorInstance(403, "用户名已被使用");
        }

        User build = User.builder()
                .username(username)
                .password(password)
                .lastLoginTime(new Date())
                .build();

        userService.save(build);

        JwtAuthenticatioToken jwtAuthenticatioToken = SecurityUtil.login(request, username, "fakePassword", authenticationManager, JSON.toJSONString(one));

        return ResultBody.newSuccessInstance(jwtAuthenticatioToken.getToken());
    }

    @ApiOperation("登陆")
    @GetMapping("/login")
    public ResultBody login(@NotNull String username, @NotNull String password, HttpServletRequest request) {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("username", username);
        wrapper.eq("password", password);
        wrapper.eq("active_status", true);
        User one = userService.getOne(wrapper);
        if (one == null) {
            return ResultBody.newErrorInstance(403, "用户名或密码错误");
        }
        JwtAuthenticatioToken jwtAuthenticatioToken = SecurityUtil.login(request, username, "fakePassword", authenticationManager, JSON.toJSONString(one));

        UpdateWrapper<User> u = new UpdateWrapper<>();
        u.eq("id", one.getId());
        u.set("last_login_time", new Date());
        userService.update(u);

        return ResultBody.newSuccessInstance(jwtAuthenticatioToken.getToken());
    }

    @GetMapping("/info")
    @ApiOperation("用户信息")
    @JwtTokenInit
    public ResultBody getInfo(@ApiIgnore JWTToken jwtToken) {

        QueryWrapper<User> q = new QueryWrapper<>();
        q.eq("id", jwtToken.getId());
        q.select("id", "username", "email", "last_login_time");
        User one = userService.getOne(q);

        QueryWrapper<APIKey> qw = new QueryWrapper<>();
        qw.eq("user_id", jwtToken.getId());
        qw.eq("active", true);
        APIKey apiKey = apiKeyService.getOne(qw);
        if (apiKey != null) {
            one.setApikey(apiKey.getApikey());
            one.setExpireTime(apiKey.getExpireTime());
        }

        return ResultBody.newSuccessInstance(one);
    }

    @GetMapping("/bindEmail")
    @JwtTokenInit
    public ResultBody bindEmail(@Email String email, @ApiIgnore JWTToken jwtToken) {

        UpdateWrapper<User> u = new UpdateWrapper<>();
        u.set("email", email);
        u.eq("id", jwtToken.getId());
        userService.update(u);

        return ResultBody.newSuccessInstance();
    }

}
