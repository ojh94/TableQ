package com.itschool.tableq.service;

import com.itschool.tableq.domain.Owner;
import com.itschool.tableq.network.request.AddOwnerRequest;
import com.itschool.tableq.repository.OwnerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
public class OwnerService {
    private final OwnerRepository ownerRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public Long save(AddOwnerRequest dto) {
        return ownerRepository.save(Owner.builder()
                .username(dto.getUsername())
                .password(dto.getPassword())
                .name(dto.getName())
                .phone_number(dto.getPhoneNumber())
                .created_at(LocalDateTime.now())
                .last_modified_at(LocalDateTime.now())
                .build()).getId();
    }

    public List<Owner> findAll() {
        return ownerRepository.findAll();
    }

}
