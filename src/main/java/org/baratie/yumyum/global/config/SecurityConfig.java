package org.baratie.yumyum.global.config;

import lombok.RequiredArgsConstructor;
import org.baratie.yumyum.domain.member.Filter.JwtAuthenticationFilter;
import org.baratie.yumyum.domain.member.handler.OAuth2SuccessHandler;
import org.baratie.yumyum.domain.member.service.JwtService;
import org.baratie.yumyum.global.exception.AuthEntryPoint;
import org.baratie.yumyum.global.exception.JwtAccessDeniedHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final AuthEntryPoint authEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
    private final JwtService jwtService;
    private final DefaultOAuth2UserService oAuth2Service;
    private final OAuth2SuccessHandler oAuth2SucessHandler;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(Arrays.asList("http://localhost:3000"));
        config.setAllowedMethods(Arrays.asList("*"));
        config.setAllowedHeaders(Arrays.asList("*"));
        config.setAllowCredentials(true);
        config.applyPermitDefaultValues();

        source.registerCorsConfiguration("/**", config);
        return source;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.cors(corsConfigurer -> corsConfigurer.configurationSource(corsConfigurationSource()))
                .csrf(csrfConfigurer -> csrfConfigurer.disable())
        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http.authorizeRequests(auth -> auth
//                .requestMatchers("/member", "/member/login", "/member/oauth2", "/review/**",
//                        "/login/oauth2/code/*","/oauth2/**", "/oauth2.0/**",
//                        "/swagger-ui/*", "/api/swagger-config", "/v3/api-docs/**").permitAll()
                .requestMatchers("/**").permitAll()
                .anyRequest().authenticated());

        http.exceptionHandling()
                .authenticationEntryPoint(authEntryPoint)
                .accessDeniedHandler(jwtAccessDeniedHandler);


        http.oauth2Login()
//                .authorizationEndpoint(auth -> auth.baseUri("/oauth2/authorization/naver"))
//                .redirectionEndpoint(redirect -> redirect.baseUri("/login/oauth2/code/*"))
                .userInfoEndpoint()
                .userService(oAuth2Service)
                .and()
                .successHandler(oAuth2SucessHandler);


        http.addFilterBefore(new JwtAuthenticationFilter(jwtService), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}