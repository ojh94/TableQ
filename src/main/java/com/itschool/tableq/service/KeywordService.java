package com.itschool.tableq.service;

import com.itschool.tableq.domain.Keyword;
import com.itschool.tableq.network.Header;
import com.itschool.tableq.network.request.KeywordRequest;
import com.itschool.tableq.network.response.KeywordResponse;
import com.itschool.tableq.repository.KeywordRepository;
import com.itschool.tableq.service.base.BaseService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class KeywordService extends BaseService<KeywordRequest, KeywordResponse, Keyword> {

    // 생성자
    @Autowired
    public KeywordService(KeywordRepository baseRepository) {
        super(baseRepository);
    }

    @Override
    protected KeywordRepository getBaseRepository() {
        return (KeywordRepository) baseRepository;
    }

    @Override
    protected KeywordResponse response(Keyword keyword) {
        return KeywordResponse.of(keyword);
    }

    @Override
    protected Keyword convertBaseEntityFromRequest(KeywordRequest requestEntity) {
        return Keyword.builder()
                .name(requestEntity.getName())
                .build();
    }

    public List<Keyword> findAll(){return getBaseRepository().findAll();}

}
