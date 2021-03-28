package com.realeye.frontend.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.realeye.frontend.entity.User;
import com.realeye.frontend.service.UserService;
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
import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("/user")
@Api(tags = "用户登陆 详情相关")
@Validated
public class UserController {

    @Resource
    private AuthenticationManager authenticationManager;

    @Resource
    private UserService userService;

    @ApiOperation("登陆")
    @GetMapping("/login")
    public ResultBody login(@NotNull String username, @NotNull String password, HttpServletRequest request) {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("username", username);
        wrapper.eq("password", password);
        User one = userService.getOne(wrapper);
        if (one == null) {
            return ResultBody.newErrorInstance(403, "用户名或密码错误");
        }
        JwtAuthenticatioToken jwtAuthenticatioToken = SecurityUtil.login(request, username, "fakePassword", authenticationManager, JSON.toJSONString(one));

        return ResultBody.newSuccessInstance(jwtAuthenticatioToken.getToken());
    }

    @GetMapping("/info")
    @ApiOperation("用户信息")
    @JwtTokenInit
    public ResultBody getInfo(@ApiIgnore JWTToken jwtToken){
        return ResultBody.newSuccessInstance();
    }

}
