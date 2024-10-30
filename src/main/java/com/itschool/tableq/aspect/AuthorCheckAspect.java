package com.itschool.tableq.aspect;

import com.itschool.tableq.service.UserDetailService;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.nio.file.AccessDeniedException;

// Aspect를 통해 @AuthorCheck가 적용된 메서드에 대해 작성자 확인 로직을 수행
@Aspect
@Component
@RequiredArgsConstructor
public class AuthorCheckAspect {

    private final UserDetailService userDetailService;

    @Before("@annotation(AuthorCheck) && args(id, ..)")
    public void checkAuthor(JoinPoint joinPoint, Long userId) throws AccessDeniedException {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getAuthorities();

        boolean isAuthor = userDetailService.isAuthor(userDetails, userId);

        if(!isAuthor) {
            throw new AccessDeniedException("작성자만 삭제할 수 있습니다.");
        }
    }
}
