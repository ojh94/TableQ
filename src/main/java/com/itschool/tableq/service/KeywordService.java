package com.itschool.tableq.service;

import com.itschool.tableq.domain.Keyword;
import com.itschool.tableq.network.Header;
import com.itschool.tableq.network.response.KeywordResponse;
import com.itschool.tableq.repository.KeywordRepository;
import com.itschool.tableq.service.base.BaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class KeywordService extends BaseService<Keyword, KeywordResponse, Keyword> {

    private final KeywordRepository keywordRepository;

    public List<Keyword> findAll(){return keywordRepository.findAll();}

    @Override
    protected KeywordResponse response(Keyword keyword) {
        return null;
    }

    @Override
    public Header<KeywordResponse> create(Header<Keyword> request) {
        return null;
    }

    @Override
    public Header<KeywordResponse> read(Long id) {
        return null;
    }

    @Override
    public Header<KeywordResponse> update(Long id, Header<Keyword> request) {
        return null;
    }

    @Override
    public Header delete(Long id) {
        return null;
    }
}
