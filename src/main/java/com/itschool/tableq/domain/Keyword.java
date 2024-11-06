package com.itschool.tableq.domain;

import com.itschool.tableq.domain.base.AuditableEntity;
import com.itschool.tableq.network.request.KeywordRequest;
import jakarta.persistence.*;
import lombok.*;

@Table(name = "keywords")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString
@Entity
public class Keyword extends AuditableEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Long id;

    @Column(nullable = false,unique = true)
    private String name;

    @Builder
    public Keyword(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public void update(KeywordRequest keywordRequest) {
        this.name = keywordRequest.getName();
    }
}
