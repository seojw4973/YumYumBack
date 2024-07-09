package org.baratie.yumyum.domain.report.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ReportType {

    STORE("가게"),
    REVIEW("리뷰"),
    REPLY("댓글");

    private final String type;


}
