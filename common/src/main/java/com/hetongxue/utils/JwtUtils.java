package com.hetongxue.utils;

import io.jsonwebtoken.*;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @Description: jwt工具类
 * @ClassNmae: JwtUtils
 * @Author: 何同学
 * @DateTime: 2022-07-04 16:11
 */
@Component
public class JwtUtils {

    /**
     * 过期时间(单位：ms)
     */
    private static final Long EXPIRATION_TIME = 259200000L;// 默认3天
    /**
     * 密钥
     */
    private static final String SECRET = "568548eddf5fe99ews458dftgv4v87gh";

    /**
     * 生成JWT
     */
    public String generateToken(Long id, String username) {
        return Jwts.builder()
                // 设置头部信息
                .setHeaderParam("typ", "JWT")
                .setHeaderParam("alg", "HS2256")
                // 设置主题
                .setSubject("userinfo")
                // 设置用户ID
                .claim("id", id)
                // 设置用户名
                .claim("username", username)
                // 设置发行时间
                .setIssuedAt(new Date())
                // 设置过期时间(claim设置在过期时间之前 否则可能会出现过期时间不生效问题)
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                // 设置签发方式
                .signWith(SignatureAlgorithm.HS512, SECRET)
                .compact();
    }

    /**
     * 解析JWT
     */
    public Claims getClaims(String token) {
        try {
            return Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token).getBody();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 判断jwt是否过期
     */
    public boolean isExpired(Claims claims) {
        return claims.getExpiration().before(new Date());
    }

}