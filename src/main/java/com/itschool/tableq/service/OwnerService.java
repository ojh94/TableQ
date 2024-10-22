package com.itschool.tableq.service;

import com.itschool.tableq.domain.Owner;
import com.itschool.tableq.network.Header;
import com.itschool.tableq.network.Response.OwnerResponse;
import com.itschool.tableq.network.request.OwnerRequest;
import com.itschool.tableq.repository.OwnerRepository;
import com.itschool.tableq.service.base.BaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
public class OwnerService extends BaseService<OwnerRequest, OwnerResponse, Owner> {
    private final OwnerRepository ownerRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;






    public Long save(OwnerRequest dto) {
        return ownerRepository.save(Owner.builder()
                .userName(dto.getUsername())
                .password(dto.getPassword())
                .name(dto.getName())
                .phoneNumber(dto.getPhoneNumber())
                .createdAt(LocalDateTime.now())
                .lastModifiedAt(LocalDateTime.now())
                .build()).getId();
    }

    public List<Owner> findAll() {
        return ownerRepository.findAll();
    }

    @Override
    protected OwnerResponse response(Owner owner) {
        return new OwnerResponse(owner);
    }

    @Override
    public Header<OwnerResponse> create(Header<OwnerRequest> request) {
        OwnerRequest ownerRequest = request.getData();

        Owner owner = Owner.builder()
                .userName(ownerRequest.getUsername())
                .password(bCryptPasswordEncoder.encode(ownerRequest.getPassword()))
                .name(ownerRequest.getName())
                .phoneNumber(ownerRequest.getPhoneNumber())
                .createdAt(LocalDateTime.now())
                .lastModifiedAt(LocalDateTime.now())
                .build();

        baseRepository.save(owner);
        return Header.OK(response(owner));
    }

    @Override
    public Header<OwnerResponse> read(Long id) {
        return Header.OK(response(baseRepository.findById(id).orElse(null)));
    }

    @Override
    public Header<OwnerResponse> update(Long id, Header<OwnerRequest> request) {
        OwnerRequest ownerRequest = request.getData();

        return baseRepository.findById(id)
                .map(owner -> {
                    owner.setUserName(ownerRequest.getUsername());
                    owner.setName(ownerRequest.getName());
                    owner.setPassword(bCryptPasswordEncoder.encode(ownerRequest.getPassword()));
                    owner.setPhoneNumber(ownerRequest.getPhoneNumber());
                    owner.setLastModifiedAt(LocalDateTime.now());

                    ownerRepository.save(owner);
                    return Header.OK(response(owner));
                })
                .orElseThrow(()-> new NotFoundException("owner not found"));
    }

    @Override
    public Header delete(Long id) {
        return baseRepository.findById(id)
                .map(owner -> {
                    ownerRepository.delete(owner);
                    return Header.OK(response(owner));
                })
                .orElseThrow(()-> new NotFoundException("owner not found"));
    }
}
