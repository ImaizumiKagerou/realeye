package com.realeye.frontend.service.impl;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.realeye.frontend.entity.APIKey;
import com.realeye.frontend.mapper.APIKeyMapper;
import com.realeye.frontend.service.APIKeyService;
@Service
public class APIKeyServiceImpl extends ServiceImpl<APIKeyMapper, APIKey> implements APIKeyService{

}
