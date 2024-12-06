package com.itschool.tableq.config.handler;

import com.itschool.tableq.domain.User;
import com.itschool.tableq.repository.UserRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;

@RequiredArgsConstructor
@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final UserRepository userRepository;
    private final RequestCache requestCache; // SavedRequest 사용

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        String username = authentication.getName(); // 로그인한 사용자의 이메일 (username)
        User user = userRepository.findByEmail(username).orElseThrow(
                () -> new RuntimeException("User not found"));

        // 현재 시간을 lastLoginAt에 설정
        user.setLastLoginAt(LocalDateTime.now());
        userRepository.save(user); // 업데이트된 사용자 정보 저장

        // SavedRequest를 사용하여 사용자가 로그인 전에 접근하려던 URL을 가져옴
        SavedRequest savedRequest = requestCache.getRequest(request, response);

        if (savedRequest != null) {
            // 로그인 전 URL이 있으면 그 URL로 리다이렉트
            response.sendRedirect(savedRequest.getRedirectUrl());
        } else {
            // 로그인 후 기본적으로 홈으로 리다이렉트
            response.sendRedirect("/"); // 원하는 리다이렉트 URL로 변경 가능
        }
    }
}
