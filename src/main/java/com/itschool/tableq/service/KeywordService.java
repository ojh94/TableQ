package com.itschool.tableq.service;

import com.itschool.tableq.domain.Keyword;
import com.itschool.tableq.network.Header;
import com.itschool.tableq.network.request.KeywordRequest;
import com.itschool.tableq.network.response.KeywordResponse;
import com.itschool.tableq.repository.KeywordRepository;
import com.itschool.tableq.service.base.BaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class KeywordService extends BaseService<KeywordRequest, KeywordResponse, Keyword> {

    private final KeywordRepository keywordRepository;

    public List<Keyword> findAll(){return keywordRepository.findAll();}

    @Override
    protected KeywordResponse response(Keyword keyword) {
        return KeywordResponse.of(keyword);
    }

    @Override
    public Header<KeywordResponse> create(Header<KeywordRequest> request) {
        KeywordRequest keywordRequest = request.getData();

        Keyword keyword = Keyword.builder()
                .name(keywordRequest.getName())
                .build();

        baseRepository.save(keyword);
        return Header.OK(response(keyword));
    }

    @Override
    public Header<KeywordResponse> read(Long id) {
        return Header.OK(response(baseRepository.findById(id).orElse(null)));
    }

    @Override
    @Transactional
    public Header<KeywordResponse> update(Long id, Header<KeywordRequest> request) {
        KeywordRequest keywordRequest = request.getData();

        Keyword keyword = baseRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("not found"));
        keyword.update(keywordRequest);
        return Header.OK(response(keyword));
    }

    @Override
    public Header delete(Long id) {
        return baseRepository.findById(id)
                .map(keyword -> {
                    baseRepository.delete(keyword);
                    return Header.OK(response(keyword));
                })
                .orElseThrow(() -> new IllegalArgumentException("not found"));
    }

}
