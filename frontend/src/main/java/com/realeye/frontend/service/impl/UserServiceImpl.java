package com.realeye.frontend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.realeye.frontend.entity.User;
import com.realeye.frontend.mapper.UserMapper;
import com.realeye.frontend.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
}

