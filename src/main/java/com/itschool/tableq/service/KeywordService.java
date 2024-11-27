package com.itschool.tableq.service;

import com.itschool.tableq.domain.Keyword;
import com.itschool.tableq.network.Header;
import com.itschool.tableq.network.request.KeywordRequest;
import com.itschool.tableq.network.response.KeywordResponse;
import com.itschool.tableq.repository.KeywordRepository;
import com.itschool.tableq.service.base.BaseService;
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

    public List<Keyword> findAll(){return getBaseRepository().findAll();}

    @Override
    public Header<KeywordResponse> create(Header<KeywordRequest> request) {
        KeywordRequest keywordRequest = request.getData();

        Keyword keyword = Keyword.builder()
                .name(keywordRequest.getName())
                .build();

        getBaseRepository().save(keyword);
        return Header.OK(response(keyword));
    }

    @Override
    public Header<KeywordResponse> read(Long id) {
        return Header.OK(response(getBaseRepository().findById(id).orElse(null)));
    }

    @Override
    @Transactional
    public Header<KeywordResponse> update(Long id, Header<KeywordRequest> request) {
        KeywordRequest keywordRequest = request.getData();

        Keyword keyword = getBaseRepository().findById(id).orElseThrow(() -> new IllegalArgumentException("not found"));
        keyword.update(keywordRequest);
        return Header.OK(response(keyword));
    }

    @Override
    public Header delete(Long id) {
        return getBaseRepository().findById(id)
                .map(keyword -> {
                    getBaseRepository().delete(keyword);
                    return Header.OK(response(keyword));
                })
                .orElseThrow(() -> new IllegalArgumentException("not found"));
    }

}
