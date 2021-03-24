package com.realeye.backend.utils.jwt;

import com.realeye.backend.utils.jwt.utils.JwtTokenUtils;
import com.realeye.backend.utils.jwt.utils.SecurityUtil;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


/**
 * 登录认证过滤器
 */
@Slf4j
public class JwtAuthenticationFilter extends BasicAuthenticationFilter {

//    private UserManager userManager = SpringContextUtil.getBean("userManager",UserManager.class);

    @Autowired
    public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        // 获取令牌并根据令牌获取登录认证信息
        Authentication authentication = null;
        String token = JwtTokenUtils.getToken(request);
        if (token != null) {
            if (SecurityUtil.getAuthentication() == null) {
                Claims claims = JwtTokenUtils.getClaimsFromToken(token);
                if (claims == null) {
                    log.error("该用户上下文中Authentication为空");
                }
                if (JwtTokenUtils.isTokenExpired(token)) {
                    log.error("token超时");
                }
                if (claims != null) {
                    String phone = claims.getSubject();
                    if (phone == null) {
                        log.error("获取用户账号为空");
                    }
                    //TODO
//                    Set<String> userPermissions = userManager.getUserPermissions(phone);
//                    List<GrantedAuthority> authorities = new ArrayList<>();
//                    for (String str:userPermissions) {
//                        authorities.add(new GrantedAuthorityImpl(str));
//                    }
                    authentication = new JwtAuthenticatioToken(phone, null, null, token);
                }
            } else {
                if (JwtTokenUtils.validateToken(token, JwtTokenUtils.getUserIdFromToken(token))) {
                    authentication = SecurityUtil.getAuthentication();
                } else {
                    log.error("token验证不正确");
                }
            }
        }

        // 设置登录认证信息到上下文
        SecurityContextHolder.getContext().setAuthentication(authentication);


        // 获取token, 并检查登录状态
        SecurityUtil.checkAuthentication(request);
        chain.doFilter(request, response);
    }

}
