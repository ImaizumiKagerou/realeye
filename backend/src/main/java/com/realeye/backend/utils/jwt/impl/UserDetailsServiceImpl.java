package com.realeye.backend.utils.jwt.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.realeye.backend.entity.AdminUser;
import com.realeye.backend.service.AdminService;
import com.realeye.backend.utils.jwt.JWTConstant;
import com.realeye.backend.utils.jwt.JwtUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service("userDetailsService")
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private AdminService adminService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        QueryWrapper<AdminUser> wrapper = new QueryWrapper<>();
        wrapper.eq("username", username);
        AdminUser one = adminService.getOne(wrapper);
        if (one == null) {
            throw new UsernameNotFoundException("该用户不存在");
        }
        // 用户权限列表，根据用户拥有的权限标识与如 @PreAuthorize("hasAuthority('sys:menu:view')") 标注的接口对比，决定是否可以调用接口
        return new JwtUserDetails(username, one.getPassword(), JWTConstant.SALT, null);
    }
}
