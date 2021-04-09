package com.realeye.frontend.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.realeye.frontend.entity.Community;
import com.realeye.frontend.entity.MyPageVO;
import com.realeye.frontend.service.CommunityService;
import com.realeye.frontend.utils.ResultBody;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.util.function.Consumer;

@RestController
@RequestMapping("/prime")
@Slf4j
@Validated
public class PrimeController {

    @Resource
    private CommunityService communityService;

    @GetMapping("/list")
    public ResultBody getList(@NotNull Integer pageNum, @NotNull Integer pageSize) {

        QueryWrapper<Community> wrapper = new QueryWrapper<>();
        wrapper.eq("parent_id", 0);
        wrapper.eq("prime", 1);
        wrapper.eq("active",true);
        wrapper.orderByDesc("create_time");
        wrapper.select("id","title","preview","username","create_time","like_count","watch_count");
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

    @GetMapping("/getPrimeArticle")
    public ResultBody getOne(@NotNull Integer id){

        QueryWrapper<Community> wrapper = new QueryWrapper<>();
        wrapper.eq("id", id);
        Community one = communityService.getOne(wrapper);

        return ResultBody.newSuccessInstance(one);
    }
}
