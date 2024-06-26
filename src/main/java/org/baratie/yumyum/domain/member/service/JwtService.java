package org.baratie.yumyum.domain.member.service;

import io.jsonwebtoken.*;
import jakarta.annotation.PostConstruct;
import org.baratie.yumyum.domain.member.domain.CustomUserDetails;
import org.baratie.yumyum.domain.member.dto.TokenDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.security.auth.Subject;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Component
public class JwtService {

    private final MemberDetails memberDetails;

    @Value("${spring.jwt.key}")
    private String key;

    @Value("${spring.jwt.live.atk}")
    private Long atkLive;

    @Value("${spring.jwt.live.rtk}")
    private Long rtkLive;

    private static final String AUTHORITIES_KEY = "auth";

    public JwtService(MemberDetails memberDetails) {
        this.memberDetails = memberDetails;
    }

    @PostConstruct
    protected void init(){ key = Base64.getEncoder().encodeToString(key.getBytes()); }

    public TokenDto createToken(Authentication authentication){
        String authorities = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.joining(","));

        String atk =  Jwts.builder()
                .setSubject(authentication.getName())
                .claim(AUTHORITIES_KEY, authorities)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + atkLive))
                .signWith(SignatureAlgorithm.HS256, key)
                .compact();

        String rtk = Jwts.builder()
                .setExpiration(new Date(System.currentTimeMillis() + rtkLive))
                .signWith(SignatureAlgorithm.HS256, key)
                .compact();

        return new TokenDto(atk, rtk);
    }

    public boolean validateToken(String token) {
        try{
            Jwts.parser().setSigningKey(key).parseClaimsJws(token);
            return true;
        }catch(io.jsonwebtoken.security.SecurityException | MalformedJwtException e){
            return false;
        }catch(ExpiredJwtException e){
            return false;
        }catch(UnsupportedJwtException e){
            return false;
        }catch(IllegalArgumentException e){
            return false;
        }
    }

    public Authentication getAuthentication(String atk) {
        Claims claims = Jwts.parser().setSigningKey(key).parseClaimsJws(atk).getBody();

        if(claims.get(AUTHORITIES_KEY) == null){
            throw new RuntimeException("권한 정보가 없는 토큰입니다.");
        }

        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        CustomUserDetails userDetails = memberDetails.loadUserByUsername(claims.getSubject());

        return new UsernamePasswordAuthenticationToken(userDetails, null, authorities);
    }
}
