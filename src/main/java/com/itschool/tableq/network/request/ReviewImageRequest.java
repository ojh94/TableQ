package com.itschool.tableq.network.request;

import com.itschool.tableq.domain.Review;
import com.itschool.tableq.network.request.base.FileRequest;
import lombok.*;

import java.time.LocalDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class ReviewImageRequest extends FileRequest {
    private Long id;

    private Review review;
}
