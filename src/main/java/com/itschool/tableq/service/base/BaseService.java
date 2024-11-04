package com.itschool.tableq.service.base;

import com.itschool.tableq.ifs.CrudInterface;
import com.itschool.tableq.network.Header;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public abstract class BaseService<Req, Res, Entity> implements CrudInterface<Req, Res> {
    @Autowired(required = false)
    protected JpaRepository<Entity, Long> baseRepository;

    public abstract Header<List<Res>> getPaginatedList(Pageable pageable);

    abstract protected Res response(Entity entity);
}
