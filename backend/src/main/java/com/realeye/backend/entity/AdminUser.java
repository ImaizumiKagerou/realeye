package com.realeye.backend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminUser {

    @TableId(type = IdType.AUTO)
    private Integer id;

    private String username;

    private String nickname;

    private String password;

    private Date createTime;

}
