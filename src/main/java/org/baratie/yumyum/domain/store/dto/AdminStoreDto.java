package org.baratie.yumyum.domain.store.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AdminStoreDto {

    private Long storeId;
    private String name;
    private String call;
    private String address;
    private Boolean isClosed;
}
