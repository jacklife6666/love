package com.love.datingapp.common.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.stereotype.Component;
import java.util.Date;

@Component
public class JwtUtils {

    private static final String SECRET_KEY = "your-secret-key-that-is-long-enough";
    private static final long EXPIRATION_TIME = 7 * 24 * 60 * 60 * 1000;

    /**
     * 【修改后】生成包含角色信息的JWT Token
     * @param userId 用户ID
     * @param role 用户角色
     * @return Token字符串
     */
    public String generateToken(Long userId, String role) { // 增加了 role 参数
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + EXPIRATION_TIME);

        return JWT.create()
                .withClaim("userId", userId)
                .withClaim("role", role) // 【新增】将角色信息存入Token
                .withIssuedAt(now)
                .withExpiresAt(expiryDate)
                .sign(Algorithm.HMAC256(SECRET_KEY));
    }

    /**
     * 校验Token并解析出用户ID
     * @param token Token字符串
     * @return 用户ID
     */
    public Long validateTokenAndGetUserId(String token) {
        try {
            DecodedJWT jwt = getDecodedJwt(token);
            return jwt.getClaim("userId").asLong();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 【新增】校验Token并解析出用户角色
     * @param token Token字符串
     * @return 用户角色
     */
    public String validateTokenAndGetRole(String token) {
        try {
            DecodedJWT jwt = getDecodedJwt(token);
            return jwt.getClaim("role").asString();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 【新增】一个私有辅助方法，用于统一的Token校验
     */
    private DecodedJWT getDecodedJwt(String token) {
        Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);
        JWTVerifier verifier = JWT.require(algorithm).build();
        return verifier.verify(token);
    }
}