package com.realeye.backend.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.realeye.backend.entity.AdminUser;
import com.realeye.backend.service.AdminService;
import com.realeye.backend.utils.ResultBody;
import com.realeye.backend.utils.jwt.JWTToken;
import com.realeye.backend.utils.jwt.JwtAuthenticatioToken;
import com.realeye.backend.utils.jwt.annotation.JwtTokenInit;
import com.realeye.backend.utils.jwt.utils.SecurityUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotBlank;

@RestController
@RequestMapping("/admin")
@Api(tags = {"后台管理员登陆相关"})
@Validated
public class AdminController {

    @Resource
    private AuthenticationManager authenticationManager;

    @Resource
    private AdminService adminService;

    @ApiOperation("登陆")
    @GetMapping("/login")
    public ResultBody login(@NotBlank String username, @NotBlank String password, HttpServletRequest request) {

        QueryWrapper<AdminUser> wrapper = new QueryWrapper<>();
        wrapper.eq("username", username);
        wrapper.eq("password", password);
        AdminUser one = adminService.getOne(wrapper);
        if (one == null) {
            return ResultBody.newErrorInstance(403, "用户名或密码错误");
        }
        JwtAuthenticatioToken jwtAuthenticatioToken = SecurityUtil.login(request, username, "fakePassword", authenticationManager, JSON.toJSONString(one));

        return ResultBody.newSuccessInstance(jwtAuthenticatioToken.getToken());
    }

    @PostMapping("/info")
    @JwtTokenInit
    public ResultBody getInfo(@ApiIgnore JWTToken jwtToken) {
        System.out.println("jwtToken = " + jwtToken);
        return ResultBody.newSuccessInstance();
    }

}
