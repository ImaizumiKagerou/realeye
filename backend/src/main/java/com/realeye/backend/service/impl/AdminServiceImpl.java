package com.realeye.backend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.realeye.backend.entity.AdminUser;
import com.realeye.backend.mapper.AdminMapper;
import com.realeye.backend.service.AdminService;
import org.springframework.stereotype.Service;

@Service
public class AdminServiceImpl extends ServiceImpl<AdminMapper, AdminUser> implements AdminService {
}
