package org.baratie.yumyum.domain.member.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.baratie.yumyum.domain.member.dto.TokenDto;
import org.baratie.yumyum.domain.member.service.JwtService;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtService jwtService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        TokenDto token = jwtService.createToken(authentication);
        System.out.println("atk = " + token.getAtk());
        System.out.println("rtk = " + token.getRtk());

        String targetUrl = UriComponentsBuilder.fromUriString("/main")
                .queryParam("atk", token.getAtk())
                .build().toUriString();

        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }
}
