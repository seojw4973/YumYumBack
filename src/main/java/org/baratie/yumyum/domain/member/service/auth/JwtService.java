package org.baratie.yumyum.domain.member.service.auth;

import io.jsonwebtoken.*;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.baratie.yumyum.domain.member.domain.CustomUserDetails;
import org.baratie.yumyum.domain.member.dto.TokenDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class JwtService {

    private final MemberDetailsService memberDetailsService;
    private final RedisService redisService;

    @Value("${spring.jwt.key}")
    private String key;

    @Value("${spring.jwt.live.atk}")
    private Long atkLive;

    @Value("${spring.jwt.live.rtk}")
    private Long rtkLive;

    private static final String AUTHORITIES_KEY = "auth";

    @PostConstruct
    protected void init(){ key = Base64.getEncoder().encodeToString(key.getBytes()); }

    public String createAtk(Authentication authentication){
        String authorities = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.joining(","));

        return Jwts.builder()
                .setSubject(authentication.getName())
                .claim(AUTHORITIES_KEY, authorities)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + atkLive))
                .signWith(SignatureAlgorithm.HS256, key)
                .compact();
    }

    public String createRtk(Authentication authentication){
        String authorities = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.joining(","));

        String rtk = Jwts.builder()
                .setSubject(authentication.getName())
                .claim(AUTHORITIES_KEY, authorities)
                .setExpiration(new Date(System.currentTimeMillis() + rtkLive))
                .signWith(SignatureAlgorithm.HS256, key)
                .compact();

        redisService.setValue(authentication.getName(), rtk);
        return rtk;
    }

    public boolean validateToken(String token) {
        try{
            Jwts.parser().setSigningKey(key).parseClaimsJws(token);
            return true;
        }catch(io.jsonwebtoken.security.SecurityException | MalformedJwtException e){
            System.out.println("Invalid JWT = " + e.getMessage());
        }catch(ExpiredJwtException e){
            System.out.println("Expired JWT = " + e.getMessage());
        }catch(UnsupportedJwtException e){
            System.out.println("Unsupported JWT = " + e.getMessage());
        }catch(IllegalArgumentException e){
            System.out.println("JWT Claims String is empty = " + e.getMessage());
        }
        return false;
    }

    public Authentication getAuthentication(String token) {
        Claims claims = Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody();

        if(claims.get(AUTHORITIES_KEY) == null){
            throw new RuntimeException("권한 정보가 없는 토큰입니다.");
        }

        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        CustomUserDetails userDetails = memberDetailsService.loadUserByUsername(claims.getSubject());

        return new UsernamePasswordAuthenticationToken(userDetails, null, authorities);
    }

    public TokenDto reissueToken(String jwt) {
        String rtk = jwt.substring(7);

        validateToken(rtk);

        Authentication authentication = getAuthentication(rtk);

        Object redisRtk = redisService.getValue(authentication.getName());
        if(Objects.isNull(redisRtk) || !redisRtk.equals(rtk)){
            throw new RuntimeException("존재하지 않는 RefreshToken입니다.");
        }
        return new TokenDto(createAtk(authentication), createRtk(authentication));
    }
}
