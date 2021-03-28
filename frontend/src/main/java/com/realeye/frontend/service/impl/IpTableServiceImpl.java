package com.realeye.frontend.service.impl;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.realeye.frontend.mapper.IpTableMapper;
import com.realeye.frontend.entity.IpTable;
import com.realeye.frontend.service.IpTableService;
@Service
public class IpTableServiceImpl extends ServiceImpl<IpTableMapper, IpTable> implements IpTableService{

}
