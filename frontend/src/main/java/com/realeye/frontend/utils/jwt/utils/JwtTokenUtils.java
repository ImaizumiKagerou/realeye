package com.realeye.frontend.utils.jwt.utils;

import com.realeye.frontend.utils.jwt.JwtAuthenticatioToken;
import com.realeye.frontend.utils.jwt.impl.GrantedAuthorityImpl;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * JWT工具类
 */
@Slf4j
@Component
public class JwtTokenUtils {

    /**
     * 用户名称
     */
    private static final String USERNAME = Claims.SUBJECT;
    /**
     * 创建时间
     */
    private static final String CREATED = "created";
    /**
     * 权限列表
     */
    public static final String AUTHORITIES = "authorities";
    /**
     * 密钥
     */
    private static final String SECRET = "abcdefgh";
    /**
     * 有效期10小时
     * 7*24 小时
     */
    private static final long EXPIRE_TIME = 7 * 24 * 60 * 60 * 1000;
//    private static final long EXPIRE_TIME = 60 * 1000;
    /**
     * 有效期1小时
     */
    private static final int EXPIRE_SECOND = 1 ;

    private static RedisTemplate<String, String> redisTemplate;



    /**
     * 生成令牌
     * @param authentication 用户
     * @return 令牌
     */
    public static String generateToken(Authentication authentication) {
        Map<String, Object> claims = new HashMap<>(3);
        claims.put(USERNAME, SecurityUtil.getUsername(authentication));
        claims.put(CREATED, new Date());
        claims.put(AUTHORITIES, authentication.getAuthorities());
        return generateToken(claims);
    }

    public static String generateToken(Authentication authentication, String another) {
        Map<String, Object> claims = new HashMap<>(3);
        claims.put(USERNAME, SecurityUtil.getUsername(authentication));
        claims.put(CREATED, new Date());
        claims.put(AUTHORITIES, authentication.getAuthorities());
        claims.put("another",another);
        return generateToken(claims);
    }

    /**
     * 从数据声明生成令牌
     * @param claims 数据声明
     * @return 令牌
     */
    private static String generateToken(Map<String, Object> claims) {
        Date expirationDate = new Date(System.currentTimeMillis() + EXPIRE_TIME);
        return Jwts.builder().setClaims(claims).setExpiration(expirationDate).signWith(SignatureAlgorithm.HS512, SECRET).compact();
    }

    @Autowired
    public JwtTokenUtils(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * 从数据声明生成令牌
     *
     * @param claims 数据声明
     * @return 令牌
     */
    private static String generateToken(Map<String, Object> claims, String username) {
        final Date createdDate = new Date();
        final Date expirationDate = new Date(createdDate.getTime() + EXPIRE_TIME);
        redisTemplate.opsForHash().put(RedisKeyEnum.KeyEnum.REDIS_AUTH_KEY.getIndex(), username, createdDate.getTime() /
                1000 + "");
        return Jwts
                .builder()
                .setClaims(claims)
                .setIssuedAt(createdDate)
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS512, SECRET)
                .compact();
    }

    /**
     * 删除token
     * @param request
     */
    public static void deleteToken(HttpServletRequest request) {
        String token = getToken(request);
        String username = getUsernameFromToken(token);
        if(username!=null) {
            redisTemplate.opsForHash().delete(RedisKeyEnum.KeyEnum.REDIS_AUTH_KEY.getIndex(), username);
        }
        if(null!=token) {
            Claims claims = getClaimsFromToken(token);
            claims.put(CREATED, DateUtil.addHourToDate(new Date(), -EXPIRE_SECOND));
            generateToken(claims, username);
        }
    }
    /**
     * 从令牌中获取用户名
     *
     * @param token 令牌
     * @return 用户名
     */
    public static String getUsernameFromToken(String token) {
        String username;
        try {
            Claims claims = getClaimsFromToken(token);
            username = claims.getSubject();
        } catch (Exception e) {
            e.printStackTrace();
            username = null;
        }
        return username;
    }

    /**
     * 根据请求令牌获取登录认证信息
     *
     * @param request 令牌
     * @return 用户名
     */
    public static Authentication getAuthenticationeFromToken(HttpServletRequest request) {
        Authentication authentication = null;
        String token = JwtTokenUtils.getToken(request);
        if (token != null) {
//            final long jwtCreateTime = getIssuedAtDateFromToken(token).getTime() / 1000;
//            final Object o = getUserJwtCreateTime(getUserIdFromToken(token));
//            if (o != null) {
//                if (jwtCreateTime < Long.parseLong(o.toString())) {
//                    log.error("该用户在不同客户端登陆，已剔除第一次登录的用户");
//                    return null;
//                }
//            }
            if (SecurityUtil.getAuthentication() == null) {
                Claims claims = getClaimsFromToken(token);
                if (claims == null) {
                    log.error("该用户上下文中Authentication为空");
                    return null;
                }
                String username = claims.getSubject();
                if (username == null) {
                    log.error("获取用户账号为空");
                    return null;
                }
                if (isTokenExpired(token)) {
                    log.error("token超时");
                    return null;
                }
                Object authors = claims.get(AUTHORITIES);
                List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
                if (authors != null && authors instanceof List) {
                    for (Object object : (List) authors) {
                        authorities.add(new GrantedAuthorityImpl((String) ((Map) object).get("authority")));
                    }
                }
                authentication = new JwtAuthenticatioToken(username, null, authorities, token);
            } else {
                if (validateToken(token, getUserIdFromToken(token))) {
                    authentication = SecurityUtil.getAuthentication();
                } else {
                    log.error("token验证不正确");
                }
            }
        }
        return authentication;
    }

    /**
     * 从令牌中获取数据声明
     *
     * @param token 令牌
     * @return 数据声明
     */
    public static Claims getClaimsFromToken(String token) {
        Claims claims;
        try {
            claims = Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token).getBody();
        } catch (Exception e) {
            log.info("getClaimsFromToken:解析token数据声明报错,错误信息:"+e.getClass().getName()+":"+e.getMessage());
            claims = null;
        }
        return claims;
    }

    /**
     * 验证令牌
     *
     * @param token
     * @param username
     * @return
     */
    public static Boolean validateToken(String token, String username) {
        String userName = getUsernameFromToken(token);
        return (Objects.equals(username,userName) && !isTokenExpired(token));
    }

    /**
     * 刷新令牌
     *
     * @param token
     * @return
     */
    public static String refreshToken(String token) {
        String refreshedToken;
        try {
            Claims claims = getClaimsFromToken(token);
            claims.put(CREATED, new Date());
            refreshedToken = generateToken(claims, claims.getSubject());
//            refreshedToken = generateToken(claims, SecurityUtil.getUsername());
        } catch (Exception e) {
            e.printStackTrace();
            refreshedToken = null;
        }
        return refreshedToken;
    }

    /**
     * 判断令牌是否过期
     *
     * @param token 令牌
     * @return 是否过期
     */
    public static Boolean isTokenExpired(String token) {
        try {
            Claims claims = getClaimsFromToken(token);
            Date expiration = claims.getExpiration();
            return expiration.before(new Date());
        } catch (Exception e) {
            log.info("isTokenExpired:解析token是否过期报错,错误信息:"+e.getClass().getName()+":"+e.getMessage());
            return false;
        }
    }

    /**
     * 获取请求token
     *
     * @param request
     * @return
     */
    public static String getToken(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        String tokenHead = "Bearer ";
        if (token == null) {
            token = request.getHeader("jwtToken");
        } else if (token.contains(tokenHead)) {
            token = token.substring(tokenHead.length());
        }
        if ("".equals(token)) {
            token = null;
        }
        return token;
    }

    /**
     * 获取该用户保存redis登陆生成 jwt 的时间
     *
     * @param username
     * @return
     */
    public static Object getUserJwtCreateTime(String username) {

        return redisTemplate.opsForHash().get(RedisKeyEnum.KeyEnum.REDIS_AUTH_KEY.getIndex(), username);
    }

    /**
     * 从token中获取jwt发布时间
     *
     * @param token
     * @return
     */
    public static Date getIssuedAtDateFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        if (null == claims) return null;
        return claims.getIssuedAt();
    }

    /**
     * 从token中获取用户名
     *
     * @param token
     * @return
     */
    public static String getUserIdFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        if (null == claims) return "";
        return claims.getSubject();
    }


}
