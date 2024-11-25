package com.itschool.tableq.service;

import com.itschool.tableq.domain.Amenity;
import com.itschool.tableq.domain.User;
import com.itschool.tableq.network.Header;
import com.itschool.tableq.network.Pagination;
import com.itschool.tableq.network.request.AmenityRequest;
import com.itschool.tableq.network.response.AmenityResponse;
import com.itschool.tableq.network.response.UserResponse;
import com.itschool.tableq.service.base.BaseService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class AmenityService extends BaseService<AmenityRequest, AmenityResponse, Amenity> {
    @Override
    public Header<List<AmenityResponse>> getPaginatedList(Pageable pageable) {
        Page<Amenity> entities =  baseRepository.findAll(pageable);

        List<AmenityResponse> amenityResponsesList = entities.stream()
                .map(entity -> response(entity))
                .collect(Collectors.toList());

        Pagination pagination = Pagination.builder()
                .totalPages(entities.getTotalPages())
                .totalElements(entities.getTotalElements())
                .currentPage(entities.getNumber())
                .currentElements(entities.getNumberOfElements())
                .build();

        return Header.OK(amenityResponsesList, pagination);
    }

    @Override
    protected AmenityResponse response(Amenity amenity) {
        return AmenityResponse.of(amenity);
    }

    @Override
    public Header<AmenityResponse> create(Header<AmenityRequest> request) {
        AmenityRequest amenityRequest = request.getData();

        Amenity amenity = Amenity.builder()
                .name(amenityRequest.getName())
                .build();

        baseRepository.save(amenity);
        return Header.OK(response(amenity));
    }

    @Override
    public Header<AmenityResponse> read(Long id) {
        return Header.OK(response(baseRepository.findById(id).orElse(null)));
    }

    @Override
    @Transactional
    public Header<AmenityResponse> update(Long id, Header<AmenityRequest> request) {
        AmenityRequest amenityRequest = request.getData();
        Amenity amenity = baseRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("not found"));
        amenity.update(amenityRequest);
        return Header.OK(response(amenity));
    }

    @Override
    public Header delete(Long id) {
        return baseRepository.findById(id)
                .map(amenity -> {
                    baseRepository.delete(amenity);
                    return Header.OK(response(amenity));
                })
                .orElseThrow(() -> new IllegalArgumentException("not found"));
    }
}
