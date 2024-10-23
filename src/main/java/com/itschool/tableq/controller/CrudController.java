package com.itschool.tableq.controller;

import com.itschool.tableq.ifs.CrudInterface;
import com.itschool.tableq.network.Header;
import com.itschool.tableq.service.base.BaseService;
import io.micrometer.observation.annotation.Observed;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Component
public abstract class CrudController<Req, Res, Entity> implements CrudInterface<Req,Res> {
    @Autowired(required = false)
    protected BaseService<Req,Res,Entity> baseService;

    @Override
    @PostMapping("")
    public Header<Res> create(@RequestBody Header<Req> request){
        log.info("{}","{}",request);
        return baseService.create(request);
    }

    @Override
    @GetMapping("{id}")
    public Header<Res> read(@PathVariable Long id){
        log.info("{}","{}","read: ",id);
        return baseService.read(id);
    }

    @Override
    @PutMapping("{id}")
    public Header<Res> update(@PathVariable Long id,
                              @RequestBody Header<Req> request) {
        log.info("{}","{}","update: ",id,request);
        return baseService.update(id,request);
    }

    @Override
    @DeleteMapping("{id}")
    public Header delete(@PathVariable Long id) {
        log.info("{}","{}","delete: ",id);
        baseService.delete(id);
        return Header.OK();
    }
}
