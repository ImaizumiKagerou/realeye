package com.realeye.frontend.utils.jwt.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.realeye.frontend.entity.User;
import com.realeye.frontend.service.UserService;
import com.realeye.frontend.utils.jwt.JWTConstant;
import com.realeye.frontend.utils.jwt.JwtUserDetails;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;


@Service("userDetailsService")
public class UserDetailsServiceImpl implements UserDetailsService {

    @Resource
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("username", username);
        User one = userService.getOne(wrapper);
        if (one == null) {
            throw new UsernameNotFoundException("该用户不存在");
        }
        // 用户权限列表，根据用户拥有的权限标识与如 @PreAuthorize("hasAuthority('sys:menu:view')") 标注的接口对比，决定是否可以调用接口
        return new JwtUserDetails(username, one.getPassword(), JWTConstant.SALT, null);
    }
}
