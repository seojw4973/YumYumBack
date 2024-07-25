package org.baratie.yumyum.domain.member.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.baratie.yumyum.domain.member.service.auth.JwtService;
import org.baratie.yumyum.domain.member.service.auth.RedisService;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtService jwtService;
    private final RedisService redisService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        String atk = jwtService.createAtk(authentication);
        String rtk = jwtService.createRtk(authentication);

//        Cookie rtkCookie = new Cookie("rtk", rtk);
//        rtkCookie.setHttpOnly(true);
//        rtkCookie.setSecure(true);
//        rtkCookie.setPath("/");
//        rtkCookie.setMaxAge(604800);
//        response.addCookie(rtkCookie);
//
//        response.addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + atk);

//        redisService.setValue(authentication.getName(), atk);

//        response.sendRedirect("http://223.130.139.146:3000/callback?atk=" + atk + "&rtk=" + rtk);
        response.sendRedirect("https://yum-yum-phi.vercel.app/callback?atk=" + atk + "&rtk=" + rtk);

    }
}
