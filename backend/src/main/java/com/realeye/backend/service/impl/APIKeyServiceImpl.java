package com.realeye.backend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.realeye.backend.entity.APIKey;
import com.realeye.backend.mapper.APIKeyMapper;
import com.realeye.backend.service.APIKeyService;
import org.springframework.stereotype.Service;

@Service
public class APIKeyServiceImpl extends ServiceImpl<APIKeyMapper, APIKey> implements APIKeyService {
}




