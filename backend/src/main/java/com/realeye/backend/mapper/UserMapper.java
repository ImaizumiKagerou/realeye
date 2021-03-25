package com.realeye.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.realeye.backend.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {
}
