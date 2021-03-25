package com.realeye.backend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class Community {

    @TableId(type = IdType.AUTO)
    private Integer id;

    private Integer parentId;

    private Integer user_id;

    private String content;

    private Date createTime;

    private Boolean active;

}
