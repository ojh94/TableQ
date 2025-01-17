package com.itschool.tableq.service;

import com.itschool.tableq.domain.User;
import com.itschool.tableq.domain.enumclass.MemberRole;
import com.itschool.tableq.network.Header;
import com.itschool.tableq.network.request.UserRequest;
import com.itschool.tableq.network.response.UserResponse;
import com.itschool.tableq.repository.UserRepository;
import com.itschool.tableq.service.base.BaseService;
import groovy.lang.DeprecationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

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
    protected User convertBaseEntityFromRequest(UserRequest requestEntity) {
        return User.builder()
                .email(requestEntity.getEmail())
                .password(bCryptPasswordEncoder.encode(requestEntity.getPassword()))
                .name(requestEntity.getName())
                .phoneNumber(requestEntity.getPhoneNumber())
                .address(requestEntity.getAddress())
                .memberRole(requestEntity.getMemberRole())
                .build();
    }

    @Override
    public Header<UserResponse> update(Long id, Header<UserRequest> request) throws DeprecationException {
        UserRequest userRequest = request.getData();

        userRequest.setPassword(bCryptPasswordEncoder.encode(userRequest.getPassword()));

        return super.update(id, request);
    }

    public Boolean checkEmail(Header<UserRequest> request) {
        return getBaseRepository().findByEmail(request.getData().getEmail()).isEmpty();
    }
    public Boolean checkPhoneNumber(Header<UserRequest> request) {
        return getBaseRepository().findByPhoneNumber(request.getData().getPhoneNumber()).isEmpty();
    }

    public List<User> getAllUsers() {
        return getBaseRepository().findAll();
    }

    public Header<UserResponse> createUserRole(Header<UserRequest> request) {
        if(request.getData() != null) {
            request.getData().setMemberRole(MemberRole.USER);
        }

        return create(request);
    }

    public Header<UserResponse> createOwnerRole(Header<UserRequest> request) {
        if(request.getData() != null) {
            request.getData().setMemberRole(MemberRole.OWNER);
        }

        return create(request);
    }
}