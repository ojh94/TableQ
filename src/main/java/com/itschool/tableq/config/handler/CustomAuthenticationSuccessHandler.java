package com.itschool.tableq.config.handler;

import com.itschool.tableq.domain.User;
import com.itschool.tableq.repository.UserRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;

@RequiredArgsConstructor
@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final UserRepository userRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        String username = authentication.getName(); // 로그인한 사용자의 이메일 (username)
        User user = userRepository.findByEmail(username).orElseThrow(
                () -> new RuntimeException("User not found"));

        // 현재 시간을 lastLoginAt에 설정
        user.setLastLoginAt(LocalDateTime.now());
        userRepository.save(user); // 업데이트된 사용자 정보 저장

        // 원래의 로직 (예: 로그인 후 리다이렉션)
        try {
            response.sendRedirect("/"); // 예시로 로그인 후 홈으로 리다이렉트
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}