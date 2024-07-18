package org.baratie.yumyum.domain.member.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.baratie.yumyum.domain.member.service.auth.JwtService;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtService jwtService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        String atk = jwtService.createToken(authentication);
        String rtk = jwtService.createRtk(authentication);
        System.out.println("atk = " + atk);
        System.out.println("rtk = " + rtk);

        response.addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + atk);
        response.sendRedirect("http://223.130.139.146:3000/callback?atk=" + atk + "&rtk=" + rtk);
    }
}
