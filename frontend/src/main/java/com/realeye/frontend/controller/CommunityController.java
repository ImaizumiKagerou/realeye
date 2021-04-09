package com.realeye.frontend.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.realeye.frontend.entity.Comment;
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
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/community")
@Api(tags = "社区相关")
@Validated
public class CommunityController {

    @Resource
    private CommunityService communityService;

    @GetMapping("/list")
    public ResultBody getList(@NotNull Integer pageNum, @NotNull Integer pageSize, String val) {

        QueryWrapper<Community> wrapper = new QueryWrapper<>();
        wrapper.eq("parent_id", 0);
        wrapper.eq("active", true);
        wrapper.eq("prime", false);

        if (val == null) {
            wrapper.orderByDesc("create_time");
        } else {
            wrapper.orderByDesc("like_count");
        }

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

    @GetMapping("/info")
    public ResultBody getInfo(@NotNull Integer id) {

        UpdateWrapper<Community> u = new UpdateWrapper<>();
        u.setSql("watch_count = watch_count + 1");
        u.eq("id", id);
        communityService.update(u);

        QueryWrapper<Community> q = new QueryWrapper<>();
        q.eq("id", id);
        Community one = communityService.getOne(q);

        return ResultBody.newSuccessInstance(one);

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

    @GetMapping("/getComment")
    public ResultBody getComment(@NotNull Integer id) {

        QueryWrapper<Community> q = new QueryWrapper<>();
        q.eq("parent_id", id);
        q.eq("active", true);
        List<Community> list = communityService.list(q);
        List<Comment> collect = list.stream().map(new Function<Community, Comment>() {
            @Override
            public Comment apply(Community community) {
                return Comment.builder()
                        .id(community.getId())
                        .fromUserName(community.getUsername())
                        .content(community.getContent())
                        .createTime(community.getCreateTime().getTime())
                        .build();
            }
        }).collect(Collectors.toList());

        return ResultBody.newSuccessInstance(collect);
    }

    @JwtTokenInit
    @GetMapping("/addComment")
    public ResultBody addCommunityComment(@NotBlank String content, @NotNull Integer communityId, @ApiIgnore JWTToken jwtToken) {

        UpdateWrapper<Community> u = new UpdateWrapper<>();
        u.eq("id", communityId);
        u.setSql("comment_count = comment_count + 1");
        communityService.update(u);

        Community build = Community.builder()
                .content(content)
                .active(true)
                .userId(jwtToken.getId())
                .username(jwtToken.getUsername())
                .parentId(communityId)
                .prime(false)
                .build();
        communityService.save(build);
        return ResultBody.newSuccessInstance();
    }

    @GetMapping("/like")
    public ResultBody LikeCommunity(@NotNull Integer id) {
        UpdateWrapper<Community> u = new UpdateWrapper<>();

        u.eq("id", id);
        u.setSql("watch_count = watch_count - 1");
        u.setSql("like_count = like_count + 1");
        communityService.update(u);

        return ResultBody.newSuccessInstance();
    }

}
