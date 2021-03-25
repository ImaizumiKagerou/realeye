package com.realeye.backend.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.realeye.backend.entity.Community;
import com.realeye.backend.service.CommunityService;
import com.realeye.backend.utils.ResultBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("/community")
public class CommunityController {

    @Resource
    private CommunityService communityService;

    @GetMapping("/getList")
    public ResultBody getList(@NotNull Integer pageNum, @NotNull Integer pageSize) {
        QueryWrapper<Community> wrapper = new QueryWrapper<>();
        wrapper.eq("parent_id", 0);
        wrapper.orderByAsc("create_time");
        Page<Community> page = new Page<>(pageNum, pageSize);
        Page<Community> list = communityService.page(page, wrapper);
        return ResultBody.newSuccessInstance(list);
    }

    @GetMapping("/delete")
    public ResultBody delete(@NotNull Integer id) {
        UpdateWrapper<Community> wrapper = new UpdateWrapper<>();
        wrapper.setSql("active=false");
        wrapper.eq("id", id);
        communityService.update(wrapper);
        return ResultBody.newSuccessInstance();
    }

    @GetMapping("/prime")
    public ResultBody prime(@NotNull Integer id) {
        UpdateWrapper<Community> wrapper = new UpdateWrapper<>();
        wrapper.setSql("prime=true");
        wrapper.eq("id", id);
        communityService.update(wrapper);
        return ResultBody.newSuccessInstance();
    }

}
