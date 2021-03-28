package com.realeye.frontend.service.impl;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.realeye.frontend.mapper.UrlTableMapper;
import com.realeye.frontend.entity.UrlTable;
import com.realeye.frontend.service.UrlTableService;
@Service
public class UrlTableServiceImpl extends ServiceImpl<UrlTableMapper, UrlTable> implements UrlTableService{

}
