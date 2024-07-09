package org.baratie.yumyum.domain.report.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.baratie.yumyum.domain.member.domain.Member;
import org.baratie.yumyum.domain.report.domain.Report;
import org.baratie.yumyum.domain.report.domain.ReportType;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateReportDto {

    private ReportType reportType;
    private String content;
    private Long targetId;

    public Report toEntity(Member member) {
        return Report.builder()
                .type(this.reportType)
                .content(this.content)
                .targetId(this.targetId)
                .member(member)
                .build();
    }
}
