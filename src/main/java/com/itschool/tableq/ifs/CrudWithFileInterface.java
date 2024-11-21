package com.itschool.tableq.ifs;

import com.itschool.tableq.network.Header;
import org.springframework.web.multipart.MultipartFile;

public interface CrudWithFileInterface<Req, Res> {

    Header<Res> create(String requestJson, MultipartFile file);

    Header<Res> read(Long id);

    Header<Res> update(Long id, String requestJson, MultipartFile file);

    Header delete(Long id);
}