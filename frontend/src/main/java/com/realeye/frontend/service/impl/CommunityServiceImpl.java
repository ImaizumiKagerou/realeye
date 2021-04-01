package com.realeye.frontend.service.impl;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.realeye.frontend.mapper.CommunityMapper;
import com.realeye.frontend.entity.Community;
import com.realeye.frontend.service.CommunityService;

@Service
public class CommunityServiceImpl extends ServiceImpl<CommunityMapper, Community> implements CommunityService {

}


