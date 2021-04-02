package com.realeye.backend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "community")
public class Community {
    @TableId(value = "id", type = IdType.INPUT)
    private Integer id;

    @TableField(value = "parent_id")
    private Integer parentId;

    @TableField(value = "user_id")
    private Integer userId;

    @TableField(value = "username")
    private String username;

    @TableField(value = "title")
    private String title;

    @TableField(value = "content")
    private String content;

    @TableField(value = "watch_count")
    private Integer watchCount;

    @TableField(value = "create_time")
    private Date createTime;

    @TableField(value = "active")
    private Boolean active;

    @TableField(value = "prime")
    private Boolean prime;
}