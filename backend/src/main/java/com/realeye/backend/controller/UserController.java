package com.realeye.backend.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.realeye.backend.entity.User;
import com.realeye.backend.service.UserService;
import com.realeye.backend.utils.ResultBody;
import io.swagger.annotations.Api;
import org.apache.commons.lang3.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("/user")
@Api(tags = "用户管理菜单相关")
@Validated
public class UserController {

    @Resource
    private UserService userService;

    @GetMapping("/getList")
    public ResultBody getList(@NotNull Integer pageNum, @NotNull Integer pageSize, Integer activeStatus, String username) {

        Page<User> page = new Page<>(pageNum, pageSize);
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        if (activeStatus != null) {
            wrapper.eq("active_status", activeStatus == 1 ? 1 : 0);
        }
        if (StringUtils.isNotBlank(username)) {
            wrapper.like("username", username);
        }
        wrapper.orderByDesc("create_time");
        wrapper.select(User.class, tableFieldInfo -> !tableFieldInfo.getColumn().equals("password"));
        Page<User> list = userService.page(page, wrapper);
        return ResultBody.newSuccessInstance(list);

    }

    @GetMapping("/changeActiveStatus")
    public ResultBody changeActiveStatus(@NotNull Integer id) {

        UpdateWrapper<User> w = new UpdateWrapper<>();
        w.eq("id", id);
        w.setSql("active_status = !active_status");
        userService.update(w);

        return ResultBody.newSuccessInstance();
    }

}
