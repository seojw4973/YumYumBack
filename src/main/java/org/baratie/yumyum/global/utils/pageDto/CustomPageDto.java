package org.baratie.yumyum.global.utils.pageDto;

import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
public class CustomPageDto {
    private List<?> content;
    private Long totalElements;

    public CustomPageDto(Page<?> page) {
        this.content = page.getContent();
        this.totalElements = page.getTotalElements();
    }
}
