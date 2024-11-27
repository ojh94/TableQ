package com.itschool.tableq.service;

import com.itschool.tableq.domain.Amenity;
import com.itschool.tableq.network.Header;
import com.itschool.tableq.network.request.AmenityRequest;
import com.itschool.tableq.network.response.AmenityResponse;
import com.itschool.tableq.repository.AmenityRepository;
import com.itschool.tableq.service.base.BaseService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AmenityService extends BaseService<AmenityRequest, AmenityResponse, Amenity> {

    // 생성자
    @Autowired
    public AmenityService(AmenityRepository baseRepository) {
        super(baseRepository);
    }

    @Override
    protected AmenityRepository getBaseRepository() {
        return (AmenityRepository) baseRepository;
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

        getBaseRepository().save(amenity);
        return Header.OK(response(amenity));
    }

    @Override
    public Header<AmenityResponse> read(Long id) {
        return Header.OK(response(getBaseRepository().findById(id).orElse(null)));
    }

    @Override
    @Transactional
    public Header<AmenityResponse> update(Long id, Header<AmenityRequest> request) {
        AmenityRequest amenityRequest = request.getData();
        Amenity amenity = getBaseRepository().findById(id).orElseThrow(() -> new IllegalArgumentException("not found"));
        amenity.update(amenityRequest);
        return Header.OK(response(amenity));
    }

    @Override
    public Header delete(Long id) {
        return getBaseRepository().findById(id)
                .map(amenity -> {
                    getBaseRepository().delete(amenity);
                    return Header.OK(response(amenity));
                })
                .orElseThrow(() -> new EntityNotFoundException());
    }
}
