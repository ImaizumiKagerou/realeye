package com.realeye.frontend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.realeye.frontend.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {
}
