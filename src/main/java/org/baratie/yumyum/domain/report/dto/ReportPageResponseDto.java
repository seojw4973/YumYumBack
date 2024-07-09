package org.baratie.yumyum.domain.report.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReportPageResponseDto {

    private Long reportId;
    private String nickName;
    private String targetContent;
    private Long targetId;
    private String content;
    private LocalDateTime createdAt;
}
