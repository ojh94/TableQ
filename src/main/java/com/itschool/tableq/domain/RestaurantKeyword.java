package com.itschool.tableq.domain;

import com.itschool.tableq.domain.base.AuditableEntity;
import com.itschool.tableq.network.request.RestaurantKeywordRequest;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "restaurant_keywords")
public class RestaurantKeyword extends AuditableEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", updatable = false)
    private Restaurant restaurant;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "keyword_id", updatable = false)
    private Keyword keyword;
}

