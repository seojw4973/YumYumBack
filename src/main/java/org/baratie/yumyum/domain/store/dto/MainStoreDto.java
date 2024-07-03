package org.baratie.yumyum.domain.store.dto;

import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class MainStoreDto {

    private String name;
    private double avgGrade;
    private int reviewCount;
    private int favoriteCount;
}
