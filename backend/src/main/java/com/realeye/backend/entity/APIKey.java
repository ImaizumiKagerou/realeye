package com.realeye.backend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@TableName("apikey")
@Builder
public class APIKey {

    @TableId(type = IdType.AUTO)
    private Integer id;

    private Integer userId;

    private String apikey;

    private Date expireTime;

}
