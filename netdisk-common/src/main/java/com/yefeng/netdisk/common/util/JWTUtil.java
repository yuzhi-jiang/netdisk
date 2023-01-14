package com.yefeng.netdisk.common.util;



import com.yefeng.netdisk.common.exception.TokenException;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 夜枫
 */
@Slf4j
public class JWTUtil {

    /**
     * 创建对象主体
     */
    private static final String CLAIM_KEY_USERNAME = "subject";
    /**
     * 创建创建时间
     */
    private static final String CLAIM_KEY_CREATED = "created";

    /**
     * 创建加密盐
     */
    private static final String SECRET="sadfkjljksdafjin123,kdf";
    /**
     * 过期时间
     */
    private static final Long EXPIRE_TIME =60*60*24*7L;//单位秒  7天


    /**
     * 根据subject生成token
     */
    public static String createToken(Object subject) {
        HashMap<String, Object> claims = new HashMap<>(2);
        claims.put(CLAIM_KEY_USERNAME, subject);
        claims.put(CLAIM_KEY_CREATED, new Date());
        return createToken(claims);
    }


    /**
     * 根据subject 和claims生产token
     *
     * @param subject
     * @param claims
     * @return
     */
    public static String createToken(Object subject, HashMap<String, Object> claims) {

        claims.put(CLAIM_KEY_USERNAME, subject);
        claims.put(CLAIM_KEY_CREATED, new Date());


        return createToken(claims);
    }


    /**
     * 根据token获取subject
     */
    public static Object getSubjectFromToken(String token) {
        Object subject = "";
        try {
            Claims claims = getClaimsFromToken(token);
            subject = claims.getSubject();
        } catch (Exception e) {
            subject = null;
            log.info("error:{}", "subject未能获取 from token");
        }
        return subject;
    }

    /**
     * 从token中获取荷载
     */
    private static Claims getClaimsFromToken(String token) {
        Claims claims = null;
        try {
//            claims = Jwts.parser()
//                    .setSigningKey(SECRET)
////                    .setAllowedClockSkewSeconds()//允许偏差的秒数
//                    .parseClaimsJws(token)
//                    .getBody();

             claims = Jwts.parser()
                    .setSigningKey(SECRET)
                    .parseClaimsJws(token)
                    .getBody();

        } catch (ExpiredJwtException e) {
            e.printStackTrace();
        } catch (UnsupportedJwtException e) {
            e.printStackTrace();
        } catch (MalformedJwtException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        return claims;
    }

    /**
     * 验证token有效
     * @param token
     * @return
     */
    public static boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .setSigningKey(SECRET)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            if (log.isDebugEnabled()) {
                log.debug("token:{} is Expired", token);
            }
            throw new TokenException("token 已过期");
        } catch (UnsupportedJwtException e) {
            if (log.isDebugEnabled()) {
                log.debug("token:{} is invalid", token);
            }
            throw new TokenException("本系统不支持该JWT，请勿篡改token");
        } catch (MalformedJwtException |SignatureException |IllegalArgumentException e) {
            if (log.isDebugEnabled()) {
                log.debug("token:{} is invalid", token);
            }
            throw new TokenException("token 是非法的，请勿篡改token");
        }

        return true;
    }

    /**
     * 根据负载生成jwt token
     */
    private static String createToken(Map<String, Object> claims) {
        //jjwt构建jwt builder
        //设置信息，过期时间，signnature
//
//        JWTCreator.Builder builder = JWT.create();
//        builder.withExpiresAt(expirationDate());
//
//
//        String token = builder.sign(Algorithm.HMAC256(SECRET));

        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(expirationDate())
                .signWith(SignatureAlgorithm.HS256, SECRET)
                .compact();
    }

    /**
     * 生成token失效时间
     */
    private static Date expirationDate() {
        //失效时间为：系统当前毫秒数+我们设置的时间（s）*1000=》毫秒
        //其实就是未来7天
        return new Date(System.currentTimeMillis() + EXPIRE_TIME * 1000);
    }


    /**
     * 判断token、是否失效
     * 失效返回true
     */
    private static boolean isTokenExpired(String token) {
        Date expiredDate = getExpiredDateFeomToken(token);
        return expiredDate.before(new Date());
    }

    /**
     * 从荷载中获取时间
     */
    private static Date getExpiredDateFeomToken(String token) {
        Claims claims = getClaimsFromToken(token);
        return claims.getExpiration();
    }

    /**
     * 判断token是否可以被刷新
     * 过期（销毁）就可以
     */
    public static boolean canBeRefreshed(String token) {
        return !isTokenExpired(token);
    }

    /**
     * 刷新token
     */
    public static String refreshToken(String token) {
        Claims claims = getClaimsFromToken(token);
        //修改为当前时间
        claims.put(CLAIM_KEY_CREATED, new Date());
        return createToken(claims);
    }


    public static void main(String[] args) throws InterruptedException {
        String token = createToken(1234324);
        System.out.println(token);
        System.out.println(getExpiredDateFeomToken(token));

        Claims claims = getClaimsFromToken(token);
        System.out.println(claims);


        token="eyJhbGciOiJIUzI1NiJ9.eyJleHAiOjE2NzM2ODU2NDAsInN1YmplY3QiOjEyMzQzMjQsImNyZWF0ZWQiOjE2NzM2ODUxNDA0MDR9.H4OdsrCQyb8PVOZKVttPDcbpUSBBG-OU-DFe4KdNs8A";

//        Thread.sleep(6000);
        validateToken(token);

    }
}

