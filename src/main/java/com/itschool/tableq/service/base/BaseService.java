package com.itschool.tableq.service.base;

import com.itschool.tableq.domain.base.AuditableEntity;
import com.itschool.tableq.ifs.CrudInterface;
import com.itschool.tableq.network.Header;
import com.itschool.tableq.network.Pagination;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public abstract class BaseService<Req, Res, Entity extends AuditableEntity> implements CrudInterface<Req, Res> {

    protected final JpaRepository<Entity, Long> baseRepository;

    // 생성자 주입을 통해 baseRepository를 주입받음
    protected BaseService(JpaRepository<Entity, Long> baseRepository) {
        this.baseRepository = baseRepository;
    }

    protected abstract JpaRepository<Entity, Long> getBaseRepository();

    protected abstract Res response(Entity entity);

    protected final List<Res> responseList(List<Entity> entities) {
        List<Res> responseList = new ArrayList<>();

        for(Entity entity : entities){
            responseList.add(response(entity));
        }

        return responseList;
    }

    public final Header<List<Res>> getPaginatedList(Pageable pageable) {
        Page<Entity> entities = getBaseRepository().findAll(pageable);

        return convertPageToList(entities);
    }

    public final Header<List<Res>> convertPageToList(Page<Entity> entityPage) {

        List<Res> responsesList = entityPage.stream()
                .map(entity -> response(entity))
                .collect(Collectors.toList());

        Pagination pagination = Pagination.builder()
                .totalPages(entityPage.getTotalPages())
                .totalElements(entityPage.getTotalElements())
                .currentPage(entityPage.getNumber())
                .currentElements(entityPage.getNumberOfElements())
                .build();

        return Header.OK(responsesList, pagination);
    }
}
