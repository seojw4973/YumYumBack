package org.baratie.yumyum.domain.store.dto;

import lombok.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class MyFavoriteStoreDto {

    private String storeImageUrl;
    private String storeName;
    private String hours;
    private String address;
    private String call;
    private Long totalReviewCount;
    private Long favoriteCount;
    private Boolean favoriteStatus;
}
