package com.realeye.frontend.utils.jwt.annotation;

import com.alibaba.fastjson.JSON;
import com.realeye.frontend.utils.ResultBody;
import com.realeye.frontend.utils.jwt.JWTToken;
import com.realeye.frontend.utils.jwt.utils.JwtTokenUtils;
import io.jsonwebtoken.Claims;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Aspect
@Component
public class JwtTokenInitAspect {
    @Around(value = "@annotation(com.realeye.frontend.utils.jwt.annotation.JwtTokenInit) && execution(* com.realeye.*..*.*(..))")
    public Object aroundAspect(ProceedingJoinPoint jp) throws Throwable {
        Object[] args = jp.getArgs(); // 参数值
        List<Object> list = new ArrayList<>();
        String token;
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
        token = request.getHeader("jwtToken");
        if (StringUtils.isBlank(token)) {
            return ResultBody.newErrorInstance();
        }
        Claims claimsFromToken = JwtTokenUtils.getClaimsFromToken(token);
        String another = claimsFromToken.get("another", String.class);
        JWTToken jwtTokenVO = JSON.parseObject(another, JWTToken.class);
        for (Object arg : args) {
            if (arg != null && arg.getClass() == JWTToken.class) {
                list.add(jwtTokenVO);
            } else {
                list.add(arg);
            }
        }
        return jp.proceed(list.toArray());
    }
}
