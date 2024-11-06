package com.itschool.tableq.service;

import com.itschool.tableq.domain.User;
import com.itschool.tableq.network.Header;
import com.itschool.tableq.network.Pagination;
import com.itschool.tableq.network.response.UserResponse;
import com.itschool.tableq.network.request.UserRequest;
import com.itschool.tableq.repository.UserRepository;
import com.itschool.tableq.service.base.BaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.data.domain.Pageable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class UserService extends BaseService<UserRequest, UserResponse, User> {

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public Header<List<UserResponse>> getPaginatedList(Pageable pageable) {
        Page<User> entities =  baseRepository.findAll(pageable);

        List<UserResponse> userResponsesList = entities.stream()
                .map(entity -> response(entity))
                .collect(Collectors.toList());

        Pagination pagination = Pagination.builder()
                .totalPages(entities.getTotalPages())
                .totalElements(entities.getTotalElements())
                .currentPage(entities.getNumber())
                .currentElements(entities.getNumberOfElements())
                .build();

        return Header.OK(userResponsesList, pagination);
    }

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
                .phoneNumber(userRequest.getPhoneNumber())
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
    @Transactional
    public Header<UserResponse> update(Long id, Header<UserRequest> request) {
        UserRequest userRequest = request.getData();

        User user = baseRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("not found" + id));

        user.update(userRequest);

        return Header.OK(response(user));
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
