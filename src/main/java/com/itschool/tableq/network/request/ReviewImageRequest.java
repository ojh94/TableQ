package com.itschool.tableq.network.request;

import com.itschool.tableq.network.request.base.FileRequest;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class ReviewImageRequest extends FileRequest {
    private Long id;

    private ReviewRequest review;
}
