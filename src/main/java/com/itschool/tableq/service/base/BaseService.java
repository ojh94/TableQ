package com.itschool.tableq.service.base;

import com.itschool.tableq.ifs.CrudInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.awt.print.Pageable;
import java.util.List;

@Component
public abstract class BaseService<Req, Res, Entity> implements CrudInterface<Req, Res> {
    @Autowired(required = false)
    protected JpaRepository<Entity, Long> baseRepository;

    /*public abstract Header<List<Res>> search(Pageable pageable);*/

    abstract protected Res response(Entity entity);
}
