package com.itschool.tableq.service;

import com.itschool.tableq.domain.BuisnessInformation;
import com.itschool.tableq.domain.Owner;
import com.itschool.tableq.network.Header;
import com.itschool.tableq.network.request.BuisnessInformationRequest;
import com.itschool.tableq.network.response.BuisnessInformationResponse;
import com.itschool.tableq.repository.OwnerRepository;
import com.itschool.tableq.service.base.BaseService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class BuisnessInformationService
        extends BaseService<BuisnessInformationRequest, BuisnessInformationResponse, BuisnessInformation> {
    @Autowired
    OwnerRepository ownerRepository;

    @Override
    public Header<List<BuisnessInformationResponse>> getPaginatedList(Pageable pageable) {
        return null;
    }

    @Override
    protected BuisnessInformationResponse response(BuisnessInformation buisnessInformation) {
        return new BuisnessInformationResponse(buisnessInformation);
    }

    protected List<BuisnessInformationResponse> responseList(List<BuisnessInformation> buisnessInformations){
        List<BuisnessInformationResponse> responseList = new ArrayList<>();

        for(BuisnessInformation buisnessInformation : buisnessInformations){
            responseList.add(response(buisnessInformation));
        }

        return responseList;
    }

    @Override
    public Header<BuisnessInformationResponse> create(Header<BuisnessInformationRequest> request) {
        BuisnessInformationRequest buisnessInformationRequest = request.getData();

        BuisnessInformation buisnessInformation = BuisnessInformation.builder()
                .buisnessNumber(buisnessInformationRequest.getBuisnessNumber())
                .buisnessName(buisnessInformationRequest.getBuisnessName())
                .contactNumber(buisnessInformationRequest.getContactNumber())
                .owner(buisnessInformationRequest.getOwner())
                .build();

        baseRepository.save(buisnessInformation);
        return Header.OK(response(buisnessInformation));
    }

    @Override
    public Header<BuisnessInformationResponse> read(Long id) {
        return Header.OK(response(baseRepository.findById(id).orElse(null)));
    }

    @Override
    @Transactional
    public Header<BuisnessInformationResponse> update(Long id, Header<BuisnessInformationRequest> request) {
        BuisnessInformationRequest buisnessInformationRequest = request.getData();

        BuisnessInformation buisnessInformation = baseRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("not found" + id));

        buisnessInformation.update(buisnessInformation);
        return Header.OK(response(buisnessInformation));
    }

    @Override
    public Header delete(Long id) {
        return baseRepository.findById(id)
                .map(buisnessInformation -> {
                    baseRepository.delete(buisnessInformation);
                    return Header.OK(response(buisnessInformation));
                })
                .orElseThrow(() -> new RuntimeException("user delete fail"));
    }
}
