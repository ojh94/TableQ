package com.itschool.tableq.network.response;

import com.itschool.tableq.domain.Keyword;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class KeywordResponse {
    private Long id;
    private String name;

    public KeywordResponse(Keyword keyword) {
        this.id = keyword.getId();
        this.name = keyword.getName();
    }
}
