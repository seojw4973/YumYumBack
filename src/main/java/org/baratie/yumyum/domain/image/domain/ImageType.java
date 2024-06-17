package org.baratie.yumyum.domain.image.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ImageType {

    REVIEW("리뷰"),
    STORE("가게");

    private final String type;
}