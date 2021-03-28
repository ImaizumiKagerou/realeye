package com.realeye.frontend.service.impl;

import com.realeye.frontend.service.DomainTableService;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.realeye.frontend.mapper.DomainTableMapper;
import com.realeye.frontend.entity.DomainTable;
@Service
public class DomainTableServiceImpl extends ServiceImpl<DomainTableMapper, DomainTable> implements DomainTableService {

}
