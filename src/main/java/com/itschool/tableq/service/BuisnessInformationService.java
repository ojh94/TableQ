package com.itschool.tableq.service;

import com.itschool.tableq.domain.BuisnessInformation;
import com.itschool.tableq.domain.User;
import com.itschool.tableq.network.Header;
import com.itschool.tableq.network.Pagination;
import com.itschool.tableq.network.request.BuisnessInformationRequest;
import com.itschool.tableq.network.response.BuisnessInformationResponse;
import com.itschool.tableq.network.response.KeywordResponse;
import com.itschool.tableq.network.response.UserResponse;
import com.itschool.tableq.service.base.BaseService;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.stream.Collectors;

public class BuisnessInformationService extends BaseService<BuisnessInformationRequest, BuisnessInformationResponse, BuisnessInformation> {

    @Override
    public Header<List<BuisnessInformationResponse>> getPaginatedList(Pageable pageable) {
        Page<BuisnessInformation> entities =  baseRepository.findAll(pageable);

        List<BuisnessInformationResponse> BuisnessInformationResponsesList = entities.stream()
                .map(entity -> response(entity))
                .collect(Collectors.toList());

        Pagination pagination = Pagination.builder()
                .totalPages(entities.getTotalPages())
                .totalElements(entities.getTotalElements())
                .currentPage(entities.getNumber())
                .currentElements(entities.getNumberOfElements())
                .build();

        return Header.OK(BuisnessInformationResponsesList, pagination);
    }

    @Override
    protected BuisnessInformationResponse response(BuisnessInformation buisnessInformation) {
        return new BuisnessInformationResponse(buisnessInformation);
    }

    @Override
    public Header<BuisnessInformationResponse> create(Header<BuisnessInformationRequest> request) {
        BuisnessInformationRequest buisnessInformationRequest = request.getData();

        BuisnessInformation buisnessInformation = BuisnessInformation.builder()
                .buisnessNumber(buisnessInformationRequest.getBuisnessNumber())
                .buisnessName(buisnessInformationRequest.getBuisnessName())
                .contactNumber(buisnessInformationRequest.getContactNumber())
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
