package com.learn.hibernate.security;

import com.learn.hibernate.data.TokenVO;
import io.jsonwebtoken.*;

import java.security.PublicKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JwtTokenProvider {
    private static long validityInMilliseconds = 3600000; // 1h
    public static String createToken(String userName , String role , long userId){
        Map<String , Object> map = new HashMap<>();
        map.put("userId" , userId);
        map.put("userRole" , role);
        Date now = new Date(System.currentTimeMillis());
        Date expiration = new Date(now.getTime() + validityInMilliseconds);
        String token = Jwts.builder()
                .setSubject(userName)
                .setClaims(map)
                .setExpiration(expiration)
                .signWith(SignatureAlgorithm.RS512 , GenerateKey.getSigningKeyBytes())
                .compact();
        return token;
    }

    public static boolean validateToken(String token){
        try {
            PublicKey publicKey = GenerateKey.loadPublicKey();
            Jwts.parser().setSigningKey(publicKey).parseClaimsJws(token);
            return true;
        }catch (JwtException|IllegalArgumentException e){
//            throw new ExpiredJwtException();
            // TODO: 10/7/2018 throw appropriate exception
            return false;
        }
    }

    public static TokenVO getTokenDetail(String token){
        PublicKey publicKey = GenerateKey.loadPublicKey();

        Jws<Claims> tokClaims = Jwts.parser()
                .setSigningKey(publicKey)
                .parseClaimsJws(token);
        TokenVO tokenVO = new TokenVO();
        tokenVO.setId(((Integer)tokClaims.getBody().get("userId")).longValue());
        tokenVO.setUserName(tokClaims.getBody().getSubject());
        tokenVO.setRole((String) tokClaims.getBody().get("userRole"));
        return tokenVO;
    }


}
