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
    public ResultBody getList(@NotNull Integer pageNum, @NotNull Integer pageSize, Integer activeStatus) {

        QueryWrapper<Community> wrapper = new QueryWrapper<>();
        wrapper.eq("prime", false);
        wrapper.orderByAsc("create_time");

        if (activeStatus != null) {
            wrapper.eq("active", activeStatus);
        }

        Page<Community> page = new Page<>(pageNum, pageSize);
        Page<Community> list = communityService.page(page, wrapper);

        return ResultBody.newSuccessInstance(list);
    }

    @GetMapping("/changeActive")
    public ResultBody changeActive(@NotNull Integer id) {

        UpdateWrapper<Community> w = new UpdateWrapper<>();
        w.eq("id", id);
        w.setSql("active = !active");
        communityService.update(w);

        return ResultBody.newSuccessInstance();
    }


}
