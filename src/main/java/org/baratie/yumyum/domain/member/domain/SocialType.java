package org.baratie.yumyum.domain.member.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SocialType {

    YUMYUM("yumyum"),
    NAVER("naver"),
    KAKAO("kakao");

    private final String type;
}
