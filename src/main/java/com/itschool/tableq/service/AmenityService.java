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
    protected Amenity convertBaseEntityFromRequest(AmenityRequest requestEntity) {
        return Amenity.builder()
                .name(requestEntity.getName())
                .build();
    }
}
