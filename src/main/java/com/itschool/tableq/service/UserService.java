package com.itschool.tableq.service;

import com.itschool.tableq.domain.User;
import com.itschool.tableq.network.Header;
import com.itschool.tableq.network.request.UserRequest;
import com.itschool.tableq.network.response.UserResponse;
import com.itschool.tableq.repository.UserRepository;
import com.itschool.tableq.service.base.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserService extends BaseService<UserRequest, UserResponse, User> {

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    // 생성자
    @Autowired
    public UserService(JpaRepository<User, Long> baseRepository,
                          BCryptPasswordEncoder bCryptPasswordEncoder) {
        super(baseRepository);
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    protected UserRepository getBaseRepository() {
        return (UserRepository) baseRepository;
    }

    @Override
    protected UserResponse response(User user) {
        return UserResponse.of(user);
    }

    @Override
    public Header<UserResponse> create(Header<UserRequest> request) {
        UserRequest userRequest = request.getData();

        User user = User.builder()
                .email(userRequest.getEmail())
                .password(bCryptPasswordEncoder.encode(userRequest.getPassword()))
                .name(userRequest.getName())
                .phoneNumber(userRequest.getPhoneNumber())
                .lastLoginAt(LocalDateTime.now())
                .build();

        getBaseRepository().save(user);
        return Header.OK(response(user));
    }

    @Override
    public Header<UserResponse> read(Long id) {
        return Header.OK(response(getBaseRepository().findById(id).orElse(null)));
    }

    @Override
    @Transactional
    public Header<UserResponse> update(Long id, Header<UserRequest> request) {
        UserRequest userRequest = request.getData();

        User user = getBaseRepository().findById(id)
                .orElseThrow(() -> new IllegalArgumentException("not found" + id));

        user.update(userRequest);

        return Header.OK(response(user));
    }

    @Override
    public Header<UserResponse> delete(Long id) {
        return getBaseRepository().findById(id)
                .map(user-> {
                    getBaseRepository().delete(user);
                    return Header.OK(response(user));
                })
                .orElseThrow(() -> new RuntimeException("user delete fail"));
    }

    public Long signup(UserRequest dto) {
        return getBaseRepository().save(User.builder()
                .email(dto.getEmail())
                .password(bCryptPasswordEncoder.encode(dto.getPassword()))
                .name(dto.getName())
                .phoneNumber(dto.getPhoneNumber())
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
        return getBaseRepository().findAll();
    }
}
