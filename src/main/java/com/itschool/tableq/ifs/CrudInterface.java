package com.itschool.tableq.ifs;

import com.itschool.tableq.network.Header;

public interface CrudInterface <Req, Res> {

    Header<Res> create(Header<Req> request);

    Header<Res> read(Long id);

    Header<Res> update(Long id, Header<Req> request);

    Header delete(Long id);
}