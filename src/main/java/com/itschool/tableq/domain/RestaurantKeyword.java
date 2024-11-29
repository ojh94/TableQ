package com.itschool.tableq.domain;

import com.itschool.tableq.domain.base.AuditableEntity;
import com.itschool.tableq.network.Header;
import com.itschool.tableq.network.request.BookmarkRequest;
import com.itschool.tableq.network.request.RestaurantKeywordRequest;
import com.itschool.tableq.network.response.BookmarkResponse;
import groovy.lang.DeprecationException;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "restaurant_keywords")
public class RestaurantKeyword extends AuditableEntity<RestaurantKeywordRequest> {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", updatable = false)
    private Restaurant restaurant;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "keyword_id", updatable = false)
    private Keyword keyword;

    @Override
    public void update(RestaurantKeywordRequest requestEntity) throws DeprecationException {
        throw new DeprecationException("연결 테이블로 update 사용처 없음");
    }
}

