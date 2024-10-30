package com.itschool.tableq.aspect;

import com.itschool.tableq.service.UserDetailService;
import com.itschool.tableq.ifs.annotation.AuthorCheck; // 올바른 패키지로 import
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.nio.file.AccessDeniedException;

@Aspect
@Component
@RequiredArgsConstructor
public class AuthorCheckAspect {

    private final UserDetailService userDetailService;

    @Before("@annotation(com.itschool.tableq.ifs.annotation.AuthorCheck) && args(id, ..)")
    public void checkAuthor(JoinPoint joinPoint, Long id) throws AccessDeniedException {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        boolean isAuthor = userDetailService.isAuthor(userDetails, id);

        if(!isAuthor) {
            throw new AccessDeniedException("작성자만 삭제할 수 있습니다.");
        }
    }
}
