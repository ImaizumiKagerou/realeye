package com.realeye.backend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class User {

    @TableId(type = IdType.AUTO)
    private Integer id;

    private String username;

    private String password;

}
