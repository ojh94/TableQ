package com.itschool.tableq.service;

import com.itschool.tableq.domain.Owner;
import com.itschool.tableq.domain.User;
import com.itschool.tableq.network.Header;
import com.itschool.tableq.network.Pagination;
import com.itschool.tableq.network.response.OwnerResponse;
import com.itschool.tableq.network.request.OwnerRequest;
import com.itschool.tableq.network.response.UserResponse;
import com.itschool.tableq.repository.OwnerRepository;
import com.itschool.tableq.service.base.BaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.webjars.NotFoundException;

import org.springframework.data.domain.Pageable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class OwnerService extends BaseService<OwnerRequest, OwnerResponse, Owner> {

    private final OwnerRepository ownerRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public List<Owner> findAll() {
        return ownerRepository.findAll();
    }

    @Override
    public Header<List<OwnerResponse>> getPaginatedList(Pageable pageable) {
        Page<Owner> entities =  baseRepository.findAll(pageable);

        List<OwnerResponse> ownerResponsesList = entities.stream()
                .map(entity -> response(entity))
                .collect(Collectors.toList());

        Pagination pagination = Pagination.builder()
                .totalPages(entities.getTotalPages())
                .totalElements(entities.getTotalElements())
                .currentPage(entities.getNumber())
                .currentElements(entities.getNumberOfElements())
                .build();

        return Header.OK(ownerResponsesList, pagination);
    }

    @Override
    protected OwnerResponse response(Owner owner) {
        return new OwnerResponse(owner);
    }

    @Override
    public Header<OwnerResponse> create(Header<OwnerRequest> request) {
        OwnerRequest ownerRequest = request.getData();

        Owner owner = Owner.builder()
                .email(ownerRequest.getEmail())
                .password(bCryptPasswordEncoder.encode(ownerRequest.getPassword()))
                .name(ownerRequest.getName())
                .phoneNumber(ownerRequest.getPhoneNumber())
                .build();

        baseRepository.save(owner);
        return Header.OK(response(owner));
    }

    @Override
    public Header<OwnerResponse> read(Long id) {
        return Header.OK(response(baseRepository.findById(id).orElse(null)));
    }

    @Override
    @Transactional
    public Header<OwnerResponse> update(Long id, Header<OwnerRequest> request) {
        OwnerRequest ownerRequest = request.getData();

        Owner owner = baseRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("not found"));
        owner.update(ownerRequest);

        return Header.OK(response(owner));
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
