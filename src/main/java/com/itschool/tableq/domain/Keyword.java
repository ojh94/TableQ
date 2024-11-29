package com.itschool.tableq.domain;

import com.itschool.tableq.domain.base.AuditableEntity;
import com.itschool.tableq.network.request.KeywordRequest;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "keywords")
public class Keyword extends AuditableEntity<KeywordRequest> {

    @Column(nullable = false,unique = true)
    private String name;

    public void update(KeywordRequest requestEntity) {
        this.name = requestEntity.getName();
    }
}
