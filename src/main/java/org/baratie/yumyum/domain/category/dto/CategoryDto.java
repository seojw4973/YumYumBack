package org.baratie.yumyum.domain.category.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.baratie.yumyum.domain.category.domain.Category;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDto {

    private String name;

    public Category toEntity(){
        return Category.builder()
                .name(name)
                .build();
    }
}
