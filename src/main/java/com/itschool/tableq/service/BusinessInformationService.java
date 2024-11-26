package com.itschool.tableq.service;

import com.itschool.tableq.domain.BusinessInformation;
import com.itschool.tableq.domain.Owner;
import com.itschool.tableq.network.Header;
import com.itschool.tableq.network.request.BusinessInformationRequest;
import com.itschool.tableq.network.response.BusinessInformationResponse;
import com.itschool.tableq.repository.BusinessInformationRepository;
import com.itschool.tableq.repository.OwnerRepository;
import com.itschool.tableq.service.base.BaseService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import java.util.List;

@RequiredArgsConstructor
@Service
public class BusinessInformationService
        extends BaseService<BusinessInformationRequest, BusinessInformationResponse, BusinessInformation> {

    @Autowired
    OwnerRepository ownerRepository;

    @Override
    protected BusinessInformationResponse response(BusinessInformation businessInformation) {
        return BusinessInformationResponse.of(businessInformation);
    }

    @Override
    public Header<BusinessInformationResponse> create(Header<BusinessInformationRequest> request) {
        BusinessInformationRequest businessInformationRequest = request.getData();

        BusinessInformation businessInformation = BusinessInformation.builder()
                .businessNumber(businessInformationRequest.getBusinessNumber())
                .businessName(businessInformationRequest.getBusinessName())
                .contactNumber(businessInformationRequest.getContactNumber())
                .owner(ownerRepository.findById(businessInformationRequest.getOwner().getId())
                        .orElseThrow(()-> new EntityNotFoundException()))
                .build();

        baseRepository.save(businessInformation);
        return Header.OK(response(businessInformation));
    }

    @Override
    public Header<BusinessInformationResponse> read(Long id) {
        return Header.OK(response(baseRepository.findById(id).orElse(null)));
    }

    public Header<List<BusinessInformationResponse>> readByOwnerId(Long ownerId, Pageable pageable){
        Owner owner = ownerRepository.findById(ownerId)
                .orElseThrow(()->new NotFoundException("Not Found Owner Id: "+ownerId));

        List<BusinessInformation> businessInformationList = ((BusinessInformationRepository)baseRepository)
                .findByOwner(owner).orElse(null);

        return Header.OK(responseList(businessInformationList));
    }

    @Override
    @Transactional
    public Header<BusinessInformationResponse> update(Long id, Header<BusinessInformationRequest> request) {
        BusinessInformationRequest businessInformationRequest = request.getData();

        BusinessInformation businessInformation = baseRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("not found" + id));

        businessInformation.update(businessInformationRequest);
        return Header.OK(response(businessInformation));
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
