package com.itschool.tableq.service;

import com.itschool.tableq.domain.User;
import com.itschool.tableq.network.Header;
import com.itschool.tableq.network.Response.UserResponse;
import com.itschool.tableq.network.request.UserRequest;
import com.itschool.tableq.service.base.BaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
public class UserService extends BaseService<UserRequest, UserResponse, User> {

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    protected UserResponse response(User user) {
        return new UserResponse(user);
    }

    @Override
    public Header<UserResponse> create(Header<UserRequest> request) {
        return null;
    }

    @Override
    public Header<UserResponse> read(Long id) {
        return null;
    }

    @Override
    public Header<UserResponse> update(Long id, Header<UserRequest> request) {
        return null;
    }

    @Override
    public Header delete(Long id) {
        return null;
    }

    public Long signup(UserRequest dto) {
        return baseRepository.save(User.builder()
                .email(dto.getEmail())
                .password(bCryptPasswordEncoder.encode(dto.getPassword()))
                .name(dto.getName())
                .phoneNumber(dto.getPhoneNumber())
                .createdAt(LocalDateTime.now())
                .lastLoginAt(LocalDateTime.now())
                .build()).getId();
    }


    public List<User> getAllUsers() {
        return baseRepository.findAll();
    }
}
