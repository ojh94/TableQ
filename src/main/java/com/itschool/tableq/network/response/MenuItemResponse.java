package com.itschool.tableq.network.response;

import com.itschool.tableq.domain.MenuItem;
import com.itschool.tableq.network.response.base.FileResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
public class MenuItemResponse extends FileResponse {

    private Long id;

    private String name;

    private String price;

    private String description;

    private Boolean recommendation;

    // 정적 팩토리 메서드 추가
    public static MenuItemResponse of(MenuItem menuItem) {
        return MenuItemResponse.builder()
                .id(menuItem.getId())
                .name(menuItem.getName())
                .price(menuItem.getPrice()) // MenuItem의 price 타입도 BigDecimal로 수정 필요
                .description(menuItem.getDescription())
                .fileUrl(menuItem.getFileUrl()) // FileResponse의 필드 사용
                .recommendation(menuItem.getRecommendation())
                .build();
    }
}