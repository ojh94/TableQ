package com.itschool.tableq.config;

import com.itschool.tableq.config.handler.CustomAuthenticationSuccessHandler;
import com.itschool.tableq.domain.enumclass.MemberRole;
import com.itschool.tableq.service.UserDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final UserDetailService userDetailService;

    private final CustomAuthenticationSuccessHandler customSuccessHandler;

    // 스프링 시큐리티 기능 비활성화 : 인증과 인가를 적용하지 않는 곳 명시
    @Bean
    public WebSecurityCustomizer configure() {
        return (web -> web.ignoring()
                .requestMatchers("/h2-console/**") // h2-console는 url은 막지마라
                .requestMatchers(new AntPathRequestMatcher("/js/**")) // 정적 파일 url은 막지마라
                .requestMatchers(new AntPathRequestMatcher("/css/**")));
    }

    // 특정 HTTP 요청에 대한 웹 기반 보안 구성
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(auth -> auth // 인증, 인가 설정
                        .requestMatchers(
                                //new AntPathRequestMatcher("**"), // 운영에 반영 x
                                new AntPathRequestMatcher("/"),
                                new AntPathRequestMatcher("/css/**"),
                                new AntPathRequestMatcher("/img/**"),
                                new AntPathRequestMatcher("/js/**"),
                                new AntPathRequestMatcher("/auth"),
                                new AntPathRequestMatcher("/login"),
                                new AntPathRequestMatcher("/signup"),
                                new AntPathRequestMatcher("/api/user"), // 비회원도 회원 가입 시 필요
                                new AntPathRequestMatcher("/api-docs"), // 운영 배포 시 삭제 요망
                                new AntPathRequestMatcher("/api-docs/**"), // 운영 배포 시 삭제 요망
                                new AntPathRequestMatcher("/v3/api-docs/**"), // 운영 배포 시 삭제 요망
                                new AntPathRequestMatcher("/swagger*/**"), // 운영 배포 시 삭제 요망
                                new AntPathRequestMatcher("/swagger-resources/**") // 운영 배포 시 삭제 요망
                        ).permitAll()
                        .requestMatchers("/admin/**",
                                                   "/api/user/owner-role")
                        .hasRole(MemberRole.ADMIN.name())
                        .requestMatchers("/user/**",
                                                   "/api/**")
                        .hasRole(MemberRole.USER.name())
                        .requestMatchers("/owner/**",
                                                   "/api/**")
                        .hasRole(MemberRole.OWNER.name())
                        .anyRequest().authenticated()
                )
                .formLogin(formLogin -> formLogin // 폼 기반 로그인 설정
                        .loginPage("/login")
                        .usernameParameter("email")
                        .defaultSuccessUrl("/")
                        .successHandler(customSuccessHandler)
                )
                .logout(logout -> logout // 로그아웃 설정
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                )
                .csrf((csrf) -> csrf.ignoringRequestMatchers(
                        new AntPathRequestMatcher("/api/**"),
                        new AntPathRequestMatcher("/api-docs"),
                        new AntPathRequestMatcher("/api-docs/**"),
                        new AntPathRequestMatcher("/v3/api-docs/**")
                ).disable())
                .headers((headers) -> headers.addHeaderWriter(
                        new XFrameOptionsHeaderWriter(XFrameOptionsHeaderWriter.XFrameOptionsMode.SAMEORIGIN)
                ))
                .build();
    }

    // 인증 관리자 관련 설정 : 사용자 정보를 가져올 서비스를 재정의하거나 인증방법 등을 설정
    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http,
                                                       BCryptPasswordEncoder bCryptPasswordEncoder,
                                                       UserDetailService userDetailService) throws Exception {

        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(this.userDetailService); // 사용자 정보 서비스 설정
        authProvider.setPasswordEncoder(bCryptPasswordEncoder);
        return new ProviderManager(authProvider);
    }

    // 패스워드 인코더로 사용할 빈 등록
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
