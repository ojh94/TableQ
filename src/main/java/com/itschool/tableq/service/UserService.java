package com.itschool.tableq.service;

import com.itschool.tableq.domain.User;
import com.itschool.tableq.network.Header;
import com.itschool.tableq.network.Response.UserResponse;
import com.itschool.tableq.network.request.UserRequest;
import com.itschool.tableq.repository.UserRepository;
import com.itschool.tableq.service.base.BaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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
        UserRequest userRequest = request.getData();

        User user = User.builder()
                .email(userRequest.getEmail())
                .password(bCryptPasswordEncoder.encode(userRequest.getPassword()))
                .name(userRequest.getName())
                .phoneNumber(String.valueOf(userRequest.getPhoneNumber()))
                .createdAt(LocalDateTime.now())
                .lastLoginAt(LocalDateTime.now())
                .build();

        baseRepository.save(user);
        return Header.OK(response(user));
    }

    @Override
    public Header<UserResponse> read(Long id) {
        return Header.OK(response(baseRepository.findById(id).orElse(null)));
    }

    @Override
    public Header<UserResponse> update(Long id, Header<UserRequest> request) {
        UserRequest userRequest = request.getData();

        return baseRepository.findById(id)
                .map(user -> {
                    user.setEmail(userRequest.getEmail());
                    user.setPassword(bCryptPasswordEncoder.encode(userRequest.getPassword()));
                    user.setName(userRequest.getName());
                    user.setPhoneNumber(userRequest.getPhoneNumber());
                    user.setLastLoginAt(LocalDateTime.now());

                    baseRepository.save(user);
                    return Header.OK(response(user));
                })
                .orElseThrow(()-> new NotFoundException("user not found"));
    }

    @Override
    public Header<UserResponse> delete(Long id) {
        return baseRepository.findById(id)
                .map(user-> {
                    baseRepository.delete(user);
                    return Header.OK(response(user));
                })
                .orElseThrow(() -> new RuntimeException("user delete fail"));
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

    public Boolean checkEmail(String email) {
        return ((UserRepository)baseRepository).findByEmail(email).isEmpty();
    }
    public Boolean checkPhoneNumber(String phoneNumber) {
        return ((UserRepository)baseRepository).findByPhoneNumber(phoneNumber).isEmpty();
    }

    public List<User> getAllUsers() {
        return baseRepository.findAll();
    }
}
