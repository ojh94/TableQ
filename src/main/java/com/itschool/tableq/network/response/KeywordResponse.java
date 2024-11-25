package com.itschool.tableq.network.response;

import com.itschool.tableq.domain.Keyword;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class KeywordResponse {
    private Long id;
    private String name;

    public static KeywordResponse of(Keyword keyword) {
        return KeywordResponse.builder()
                .id(keyword.getId())
                .name(keyword.getName())
                .build();
    }
}
