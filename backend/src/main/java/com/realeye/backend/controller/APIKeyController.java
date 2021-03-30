package com.realeye.backend.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.realeye.backend.entity.APIKey;
import com.realeye.backend.entity.User;
import com.realeye.backend.service.APIKeyService;
import com.realeye.backend.service.UserService;
import com.realeye.backend.utils.ResultBody;
import com.realeye.backend.utils.TimeUtils;
import io.swagger.annotations.Api;
import org.apache.commons.lang3.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.text.ParseException;
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
    public ResultBody getList(@NotNull Integer pageNum, @NotNull Integer pageSize, String username, String apikey, Integer activeStatus) {

        Page<APIKey> page = new Page<>(pageNum, pageSize);
        QueryWrapper<APIKey> q = new QueryWrapper<>();

        if (StringUtils.isNotBlank(username)) {
            q.like("username", username);
        }
        if (StringUtils.isNotBlank(apikey)) {
            q.eq("apikey", apikey);
        }
        if (activeStatus != null) {
            if (activeStatus == 0) {
                q.lt("expire_time", new Date());
            } else {
                q.gt("expire_time", new Date());
            }
        }

        Page<APIKey> apiKeyPage = apiKeyService.page(page, q);

        return ResultBody.newSuccessInstance(apiKeyPage);
    }

    @GetMapping("/saveKey")
    public ResultBody updateKey(
            @NotNull String username,
            @NotNull @RequestParam("expireTime") String expireTimeStr) throws ParseException {

        Date expireTime = TimeUtils.String2DateAndAddOneDay(expireTimeStr);

        QueryWrapper<User> q = new QueryWrapper<>();
        q.eq("username", username);
        User one = userService.getOne(q);
        if (one == null) {
            return ResultBody.newErrorInstance(403, "查无此人");
        }

        UpdateWrapper<APIKey> wrapper = new UpdateWrapper<>();
        wrapper.eq("user_id", one.getId());
        wrapper.set("expire_time", expireTime);
        System.out.println("wrapper.getSqlSet() = " + wrapper.getSqlSet());

        APIKey build = APIKey.builder()
                .userId(one.getId())
                .username(one.getUsername())
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
    public ResultBody getUserList() {
        List<User> list = userService.list();
        return ResultBody.newSuccessInstance(list);
    }

}
