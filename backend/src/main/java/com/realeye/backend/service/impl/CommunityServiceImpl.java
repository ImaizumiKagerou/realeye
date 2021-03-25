package com.realeye.backend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.realeye.backend.entity.Community;
import com.realeye.backend.mapper.CommunityMapper;
import com.realeye.backend.service.CommunityService;
import org.springframework.stereotype.Service;

@Service
public class CommunityServiceImpl extends ServiceImpl<CommunityMapper, Community> implements CommunityService {
}
