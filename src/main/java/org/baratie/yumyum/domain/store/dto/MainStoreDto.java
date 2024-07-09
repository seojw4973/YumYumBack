package org.baratie.yumyum.domain.store.dto;

import lombok.*;

@Getter
@NoArgsConstructor
@ToString
public class MainStoreDto {
    private Long storeId;
    private String name;
    private String imageUrl;
    private double avgGrade;
    private Long reviewCount;
    private Long favoriteCount;

    @Builder
    public MainStoreDto(Long storeId, String name, String imageUrl, double avgGrade, Long reviewCount, Long favoriteCount) {

        double roundsAvgGrade = Math.round(avgGrade * 10.0) / 10.0;

        this.storeId = storeId;
        this.name = name;
        this.imageUrl = imageUrl;
        this.avgGrade = roundsAvgGrade;
        this.reviewCount = reviewCount;
        this.favoriteCount = favoriteCount;
    }
}
