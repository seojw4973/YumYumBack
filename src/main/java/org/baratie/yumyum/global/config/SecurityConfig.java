package org.baratie.yumyum.global.config;

import lombok.RequiredArgsConstructor;
import org.baratie.yumyum.domain.member.domain.Role;
import org.baratie.yumyum.domain.member.filter.JwtAuthenticationFilter;
import org.baratie.yumyum.domain.member.handler.OAuth2SuccessHandler;
import org.baratie.yumyum.domain.member.service.auth.JwtService;
import org.baratie.yumyum.domain.member.service.auth.RedisService;
import org.baratie.yumyum.global.exception.AuthEntryPoint;
import org.baratie.yumyum.global.exception.JwtAccessDeniedHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
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

import javax.lang.model.util.Elements;
import java.util.Arrays;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final AuthEntryPoint authEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
    private final JwtService jwtService;
    private final DefaultOAuth2UserService oAuth2Service;
    private final OAuth2SuccessHandler oAuth2SuccessHandler;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(Arrays.asList("http://localhost:3000", "http://192.168.0.13:3000", "http://223.130.139.146:3000", "https://223.130.139.146:3000", "https://www.baratie.site", "https://yum-yum-phi.vercel.app"));
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(Arrays.asList(HttpHeaders.ORIGIN, HttpHeaders.CONTENT_TYPE, HttpHeaders.AUTHORIZATION, "X-Requested-With", HttpHeaders.ACCEPT, HttpHeaders.SET_COOKIE, HttpHeaders.ACCESS_CONTROL_REQUEST_METHOD, HttpHeaders.ACCESS_CONTROL_REQUEST_HEADERS, HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS, HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS));
        config.setExposedHeaders(Arrays.asList("Content-Type", HttpHeaders.AUTHORIZATION, HttpHeaders.SET_COOKIE, HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS, HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS, HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS));
        config.setAllowCredentials(true);
        config.applyPermitDefaultValues();

        source.registerCorsConfiguration("/**", config);
        return source;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.cors(corsConfigurer -> corsConfigurer.configurationSource(corsConfigurationSource()))
                .csrf(csrfConfigurer -> csrfConfigurer.disable())
                .formLogin(auth -> auth.disable())
                .httpBasic(auth -> auth.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http.authorizeRequests(auth -> auth
                .requestMatchers(("/admin/**")).hasAnyRole(Role.ADMIN.name())
                .requestMatchers("/review/**", "/reply/**", "/report",
                        "/favorite", "/like", "/search", "/member/reissue", "/member/logout",
                        "/member/favorite", "/member/myReply", "/member/likeReview", "/member/myReview").authenticated()
                .requestMatchers("/**").permitAll()
                .anyRequest().authenticated());

        http.exceptionHandling()
                .authenticationEntryPoint(authEntryPoint)
                .accessDeniedHandler(jwtAccessDeniedHandler);

        http.oauth2Login()
                .authorizationEndpoint(auth -> auth.baseUri("/oauth2/authorization"))
                .redirectionEndpoint(redirect -> redirect.baseUri("/login/oauth2/code/*"))
                .userInfoEndpoint(endpoint -> endpoint.userService(oAuth2Service))
                .successHandler(oAuth2SuccessHandler);

        http.addFilterBefore(new JwtAuthenticationFilter(jwtService), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}