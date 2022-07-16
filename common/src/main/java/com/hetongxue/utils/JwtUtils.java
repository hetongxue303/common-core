package com.hetongxue.utils;

import io.jsonwebtoken.*;
import org.springframework.stereotype.Component;

import java.time.Instant;
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
     * 过期时间
     */
    private static final Long EXPIRATION_TIME = 10080000000L;
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
                // 设置发行时间
                .setIssuedAt(new Date())
                // 设置过期时间
//                .setExpiration(new Date(date.getTime() * EXPIRATION_TIME))
                .setExpiration(new Date(Instant.now().toEpochMilli() + EXPIRATION_TIME))
                // 设置用户ID
                .claim("id", id)
                // 设置用户名
                .claim("username", username)
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
        } catch (ExpiredJwtException | SignatureException | UnsupportedJwtException | MalformedJwtException |
                 IllegalArgumentException e) {
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