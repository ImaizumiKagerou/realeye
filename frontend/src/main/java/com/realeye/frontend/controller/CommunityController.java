package com.realeye.frontend.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.realeye.frontend.entity.Community;
import com.realeye.frontend.entity.MyPageVO;
import com.realeye.frontend.service.CommunityService;
import com.realeye.frontend.utils.ResultBody;
import com.realeye.frontend.utils.jwt.JWTToken;
import com.realeye.frontend.utils.jwt.annotation.JwtTokenInit;
import io.swagger.annotations.Api;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.function.Consumer;

@RestController
@RequestMapping("/community")
@Api(tags = "社区相关")
@Validated
public class CommunityController {

    @Resource
    private CommunityService communityService;

    @GetMapping("/list")
    public ResultBody getList(@NotNull Integer pageNum, @NotNull Integer pageSize) {

        QueryWrapper<Community> wrapper = new QueryWrapper<>();
        wrapper.eq("parent_id", 0);
        wrapper.orderByAsc("create_time");
        Page<Community> page = new Page<>(pageNum, pageSize);
        Page<Community> list = communityService.page(page, wrapper);

        list.getRecords().forEach(new Consumer<Community>() {
            @Override
            public void accept(Community community) {
                community.setCreateTimeL(community.getCreateTime().getTime());
            }
        });

        MyPageVO build = MyPageVO.builder()
                .data(list.getRecords())
                .hasNextPage(list.hasNext())
                .currPage(pageNum)
                .total(list.getTotal())
                .build();

        return ResultBody.newSuccessInstance(build);

    }

    @JwtTokenInit
    @GetMapping("/add")
    public ResultBody addCommunity(@NotBlank String title, @NotBlank String content, @ApiIgnore JWTToken jwtToken) {
        Community build = Community.builder()
                .title(title)
                .content(content)
                .active(true)
                .userId(jwtToken.getId())
                .username(jwtToken.getUsername())
                .parentId(0)
                .prime(false)
                .build();
        communityService.save(build);
        return ResultBody.newSuccessInstance();
    }

    @JwtTokenInit
    @GetMapping("/addComment")
    public ResultBody addCommunityComment(@NotBlank String title, @NotBlank String content, @NotNull Integer communityId, @ApiIgnore JWTToken jwtToken) {
        Community build = Community.builder()
                .title(title)
                .content(content)
                .active(true)
                .userId(jwtToken.getId())
                .parentId(communityId)
                .prime(false)
                .build();
        communityService.save(build);
        return ResultBody.newSuccessInstance();
    }

}
