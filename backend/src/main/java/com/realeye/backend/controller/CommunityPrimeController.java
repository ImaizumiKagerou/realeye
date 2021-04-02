package com.realeye.backend.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.realeye.backend.entity.Community;
import com.realeye.backend.service.CommunityService;
import com.realeye.backend.utils.ResultBody;
import com.realeye.backend.utils.jwt.JWTToken;
import com.realeye.backend.utils.jwt.annotation.JwtTokenInit;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("/communityPrime")
public class CommunityPrimeController {

    @Resource
    private CommunityService communityService;

    @GetMapping("/getList")
    public ResultBody getList(@NotNull Integer pageNum, @NotNull Integer pageSize, Integer activeStatus) {
        QueryWrapper<Community> wrapper = new QueryWrapper<>();
        wrapper.eq("parent_id", 0);
        wrapper.eq("prime", 1);

        if (activeStatus != null) {
            wrapper.eq("active", activeStatus);
        }

        wrapper.orderByAsc("create_time");
        Page<Community> page = new Page<>(pageNum, pageSize);
        Page<Community> list = communityService.page(page, wrapper);
        return ResultBody.newSuccessInstance(list);
    }

    @GetMapping("/changeActiveStatus")
    public ResultBody changeActiveStatus(@NotNull Integer id) {
        UpdateWrapper<Community> wrapper = new UpdateWrapper<>();
        wrapper.setSql("active = !active");
        wrapper.eq("id", id);
        communityService.update(wrapper);
        return ResultBody.newSuccessInstance();
    }

    @GetMapping("/addPrime")
    @JwtTokenInit
    public ResultBody prime(@NotBlank String title, @NotBlank String content, @NotBlank String preview, @ApiIgnore JWTToken jwtToken) {

        Community build = Community.builder()
                .prime(true)
                .title(title)
                .content(content)
                .preview(preview)
                .active(true)
                .userId(jwtToken.getId())
                .parentId(0)
                .username(jwtToken.getUsername())
                .build();

        communityService.save(build);

        return ResultBody.newSuccessInstance();
    }

}
