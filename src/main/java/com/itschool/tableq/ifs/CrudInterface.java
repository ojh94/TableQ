package com.itschool.tableq.ifs;

import com.itschool.tableq.network.Header;
import groovy.lang.DeprecationException;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CrudInterface <Req, Res> {

    Header<List<Res>> getPaginatedList(Pageable pageable);

    Header<Res> create(Header<Req> request);

    Header<Res> read(Long id);

    Header<Res> update(Long id, Header<Req> request) throws DeprecationException;

    Header delete(Long id);
}