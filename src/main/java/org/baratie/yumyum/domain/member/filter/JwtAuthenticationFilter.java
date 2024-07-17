package org.baratie.yumyum.domain.member.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.baratie.yumyum.domain.member.service.auth.JwtService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String jwt = request.getHeader("Authorization");
        String requestURI = request.getRequestURI();
        System.out.println("requestURI = " + requestURI);

        if (requestURI.equals("/member/login") || requestURI.equals("/member/register") || requestURI.equals("/login/oauth2/code/naver")) {
            filterChain.doFilter(request, response);
            System.out.println("필터에 걸림");
            return;
        }

        if (jwt == null || !jwt.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = jwt.substring(7);
        try {
            if (jwtService.validateToken(token)) {
                Authentication authentication = jwtService.getAuthentication(token);
                System.out.println("authentication = " + authentication);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Token validation failed");
            return;
        }

        filterChain.doFilter(request, response);
    }
}