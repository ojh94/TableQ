package com.itschool.tableq.service;

import com.itschool.tableq.domain.Owner;
import com.itschool.tableq.network.Header;
import com.itschool.tableq.network.request.OwnerRequest;
import com.itschool.tableq.network.response.OwnerResponse;
import com.itschool.tableq.repository.OwnerRepository;
import com.itschool.tableq.service.base.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.webjars.NotFoundException;

import java.util.List;

@Service
public class OwnerService extends BaseService<OwnerRequest, OwnerResponse, Owner> {

    private final OwnerRepository ownerRepository;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    // 생성자
    @Autowired
    public OwnerService(OwnerRepository baseRepository,
                        OwnerRepository ownerRepository,
                        BCryptPasswordEncoder bCryptPasswordEncoder) {
        super(baseRepository);
        this.ownerRepository = ownerRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public List<Owner> findAll() {
        return ownerRepository.findAll();
    }

    @Override
    protected OwnerRepository getBaseRepository() {
        return (OwnerRepository) baseRepository;
    }

    @Override
    protected OwnerResponse response(Owner owner) {
        return OwnerResponse.of(owner);
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

        getBaseRepository().save(owner);
        return Header.OK(response(owner));
    }

    @Override
    public Header<OwnerResponse> read(Long id) {
        return Header.OK(response(getBaseRepository().findById(id).orElse(null)));
    }

    @Override
    @Transactional
    public Header<OwnerResponse> update(Long id, Header<OwnerRequest> request) {
        OwnerRequest ownerRequest = request.getData();

        Owner owner = getBaseRepository().findById(id).orElseThrow(() -> new IllegalArgumentException("not found"));
        owner.update(ownerRequest);

        return Header.OK(response(owner));
    }

    @Override
    public Header delete(Long id) {
        return getBaseRepository().findById(id)
                .map(owner -> {
                    ownerRepository.delete(owner);
                    return Header.OK(response(owner));
                })
                .orElseThrow(()-> new NotFoundException("owner not found"));
    }
}
