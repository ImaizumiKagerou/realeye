package com.realeye.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.realeye.backend.entity.AdminUser;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AdminMapper extends BaseMapper<AdminUser> {
}
