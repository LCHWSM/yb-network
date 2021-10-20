package com.ybau.transaction.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

@ConfigurationProperties(prefix = "jwt.config")
@Data
@Component
@Slf4j
public class JwtUtil {
    private String key;         //秘钥
    private String exp;          //过期时间，不采用
    private static Date expDate = null;      //过期时间，采用

    static {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            expDate = sdf.parse("2300-01-01");
        } catch (Exception e) {
            log.error("{}", e);
        }
    }


    /**
     * 生成JWT
     *
     * @param id
     * @param subject
     * @return
     */
    public String createJWT(String id, String subject, int randomNum) {
        //id,为用户id，subject为创建人，randomNum为随机码，防止同时登陆顶号
        //生成JWT的时间
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        JwtBuilder builder = Jwts.builder()
                .setId(id)
                .setSubject(subject)
                .setIssuedAt(now)
                .signWith(SignatureAlgorithm.HS256, key)
                .claim("username", subject)
                .claim("randomNum", randomNum)
                .claim("id", id);
        builder.setExpiration(expDate);
        return builder.compact();
    }

    /**
     * 生成无账号刷新JWT
     *
     * @param phone
     * @param uuid
     * @return
     */
    public String createRefreshJWT(String phone, String uuid) {
        //phone,为用户手机号，subject为创建人，randomNum为随机码，防止同时登陆顶号
        //生成JWT的时间
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        JwtBuilder builder = Jwts.builder()
                .setId(phone)
                .setSubject(uuid)
                .setIssuedAt(now)
                .signWith(SignatureAlgorithm.HS256, key)
                .claim("phone", phone)
                .claim("uuid", uuid);
        builder.setExpiration(expDate);
        return builder.compact();
    }

    /**
     * 解析JWT
     *
     * @param jwtStr
     * @return
     */
    public Claims parseJWT(String jwtStr) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(key)
                    .parseClaimsJws(jwtStr)
                    .getBody();
            return claims;
        } catch (Exception e) {
            return null;
        }

    }


    /**
     * 获取用户名
     *
     * @param jwtStr
     * @return
     */
    public String getUsername(String jwtStr) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(key)
                    .parseClaimsJws(jwtStr)
                    .getBody();
            return claims.get("username").toString();
        } catch (Exception e) {
            return null;
        }

    }

    /**
     * 获取用户ID
     *
     * @param jwtStr
     * @return
     */
    public String getId(String jwtStr) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(key)
                    .parseClaimsJws(jwtStr)
                    .getBody();
            return claims.get("id").toString();
        } catch (Exception e) {
            return null;
        }

    }

    /**
     * 获取手机号
     *
     * @param jwtStr
     * @return
     */
    public String getPhone(String jwtStr) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(key)
                    .parseClaimsJws(jwtStr)
                    .getBody();
            return claims.get("phone").toString();
        } catch (Exception e) {
            return null;
        }

    }

    /**
     * 获取UUID
     *
     * @param jwtStr
     * @return
     */
    public String getUUID(String jwtStr) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(key)
                    .parseClaimsJws(jwtStr)
                    .getBody();
            return claims.get("uuid").toString();
        } catch (Exception e) {
            return null;
        }
    }
}
