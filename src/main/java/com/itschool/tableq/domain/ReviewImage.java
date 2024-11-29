package com.itschool.tableq.domain;

import com.itschool.tableq.domain.base.IncludeFileUrl;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "review_images")
public class ReviewImage extends IncludeFileUrl {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_id", updatable = false)
    private Review review;
}
