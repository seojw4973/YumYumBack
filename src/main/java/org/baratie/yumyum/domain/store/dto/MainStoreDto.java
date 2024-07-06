package org.baratie.yumyum.domain.store.dto;

import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class MainStoreDto {
    private Long StoreId;
    private String name;
    private String imageUrl;
    private Double avgGrade;
    private Long reviewCount;
    private Long favoriteCount;

}
