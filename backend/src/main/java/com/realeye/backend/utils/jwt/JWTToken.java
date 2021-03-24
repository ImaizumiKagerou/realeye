package com.realeye.backend.utils.jwt;

import lombok.Data;

@Data
public class JWTToken {

    private Integer id;

    private String username;

    private String password;

}
