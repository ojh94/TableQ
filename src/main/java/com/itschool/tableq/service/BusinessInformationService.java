package com.itschool.tableq.service;

import com.itschool.tableq.domain.BusinessInformation;
import com.itschool.tableq.domain.User;
import com.itschool.tableq.network.Header;
import com.itschool.tableq.network.request.BusinessInformationRequest;
import com.itschool.tableq.network.response.BusinessInformationResponse;
import com.itschool.tableq.repository.BusinessInformationRepository;
import com.itschool.tableq.repository.UserRepository;
import com.itschool.tableq.service.base.BaseService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import java.util.List;

@Service
public class BusinessInformationService extends BaseService<BusinessInformationRequest, BusinessInformationResponse, BusinessInformation> {

    private final UserRepository userRepository;

    // 생성자
    @Autowired
    public BusinessInformationService(BusinessInformationRepository baseRepository,
                                      UserRepository userRepository) {
        super(baseRepository);
        this.userRepository = userRepository;
    }

    @Override
    protected BusinessInformationRepository getBaseRepository() {
        return (BusinessInformationRepository) baseRepository;
    }

    @Override
    protected BusinessInformationResponse response(BusinessInformation businessInformation) {
        return BusinessInformationResponse.of(businessInformation);
    }

    @Override
    protected BusinessInformation convertBaseEntityFromRequest(BusinessInformationRequest requestEntity) {
        return BusinessInformation.builder()
                .businessNumber(requestEntity.getBusinessNumber())
                .businessName(requestEntity.getBusinessName())
                .contactNumber(requestEntity.getContactNumber())
                .user(userRepository.findById(requestEntity.getUserRequest().getId())
                        .orElseThrow(()-> new EntityNotFoundException()))
                .build();
    }

    public Header<List<BusinessInformationResponse>> readByOwnerId(Long userId, Pageable pageable){
        User user = userRepository.findById(userId)
                .orElseThrow(()->new NotFoundException("Not Found Owner Id: "+userId));

        List<BusinessInformation> businessInformationList = getBaseRepository().findByUser(user).orElseThrow(()-> new EntityNotFoundException());

        return Header.OK(responseList(businessInformationList));
    }
}
