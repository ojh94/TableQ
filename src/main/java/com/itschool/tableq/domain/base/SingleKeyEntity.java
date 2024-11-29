package com.itschool.tableq.domain.base;

import com.itschool.tableq.network.request.base.SingleKeyRequest;
import groovy.lang.DeprecationException;
import jakarta.persistence.*;
import lombok.Getter;

@MappedSuperclass
@Getter
public abstract class SingleKeyEntity<T, Req extends SingleKeyRequest> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, nullable = false)
    protected T id;

    public SingleKeyEntity() {}

    public abstract void update(Req requestEntity) throws DeprecationException;
}
