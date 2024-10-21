package com.itschool.tableq.service;

import com.itschool.tableq.domain.User;
import com.itschool.tableq.network.request.AddUserRequest;
import com.itschool.tableq.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public Long save(AddUserRequest dto) {
        return userRepository.save(User.builder()
                .email(dto.getEmail())
                .password(bCryptPasswordEncoder.encode(dto.getPassword()))
                .name(dto.getName())
                .phone_Number(dto.getPhoneNumber())
                .created_at(LocalDateTime.now())
                .last_login_at(LocalDateTime.now())
                .build()).getId();
    }


    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}
