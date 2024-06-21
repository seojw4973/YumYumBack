package org.baratie.yumyum.domain.member.service;

import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Base64;
import java.util.stream.Collectors;

@Component
public class JwtService {

    @Value("${spring.jwt.key}")
    private String key;

    @Value("${spring.jwt.live.atk")
    private Long atklive;

    @Value("${spring.jwt.live.rtk")
    private Long rtklive;

    protected void init(){ key = Base64.getEncoder().encodeToString(key.getBytes()); }

    public String createToken(Authentication authentication){

    }



}
