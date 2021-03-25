package com.realeye.backend.controller;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.realeye.backend.entity.APIKey;
import com.realeye.backend.entity.User;
import com.realeye.backend.service.APIKeyService;
import com.realeye.backend.service.UserService;
import com.realeye.backend.utils.ResultBody;
import io.swagger.annotations.Api;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@RestController
@Api(tags = "apiKey相关")
@RequestMapping("/apikey")
@Validated
public class APIKeyController {

    @Resource
    private APIKeyService apiKeyService;

    @Resource
    private UserService userService;

    @GetMapping("/getList")
    public ResultBody getList(@NotNull Integer pageNum, @NotNull Integer pageSize) {

        Page<APIKey> page = new Page<>(pageNum, pageSize);
        Page<APIKey> apiKeyPage = apiKeyService.page(page, null);

        return ResultBody.newSuccessInstance(apiKeyPage);
    }

    @GetMapping("/saveKey")
    public ResultBody saveKey(@NotNull Integer userId, @NotNull Date expireTime) {
        UpdateWrapper<APIKey> wrapper = new UpdateWrapper<>();
        wrapper.eq("user_id", userId);
        wrapper.setSql("expire_time=" + expireTime);
        APIKey build = APIKey.builder()
                .userId(userId)
                .expireTime(expireTime)
                .apikey(UUID.randomUUID().toString().replaceAll("-", ""))
                .build();
        apiKeyService.saveOrUpdate(build, wrapper);
        return ResultBody.newSuccessInstance();
    }

    @GetMapping("/deleteKey")
    public ResultBody deleteKey(@NotNull Integer userId) {
        UpdateWrapper<APIKey> wrapper = new UpdateWrapper<>();
        wrapper.setSql("active=false");
        wrapper.eq("user_id", userId);
        apiKeyService.update(wrapper);
        return ResultBody.newSuccessInstance();
    }

    @GetMapping("/userList")
    public ResultBody getUserList(){
        List<User> list = userService.list();
        return ResultBody.newSuccessInstance(list);
    }

}
