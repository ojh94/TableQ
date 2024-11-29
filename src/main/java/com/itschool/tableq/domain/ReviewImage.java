package com.itschool.tableq.domain;

import com.itschool.tableq.domain.base.IncludeFileUrl;
import com.itschool.tableq.network.request.ReviewImageRequestWithFile;
import jakarta.persistence.*;
import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "review_images")
public class ReviewImage extends IncludeFileUrl<ReviewImageRequestWithFile> {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_id", updatable = false)
    private Review review;

    @Override
    @Deprecated
    public void updateWithoutFileUrl(ReviewImageRequestWithFile requestEntity) {}
}
