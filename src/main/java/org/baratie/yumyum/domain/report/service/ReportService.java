package org.baratie.yumyum.domain.report.service;

import lombok.RequiredArgsConstructor;
import org.baratie.yumyum.domain.member.domain.Member;
import org.baratie.yumyum.domain.member.service.MemberService;
import org.baratie.yumyum.domain.report.domain.Report;
import org.baratie.yumyum.domain.report.dto.CreateReportDto;
import org.baratie.yumyum.domain.report.dto.ReportPageResponseDto;
import org.baratie.yumyum.domain.report.repository.ReportRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReportService {
    private final ReportRepository reportRepository;
    private final MemberService memberService;

    public Page<ReportPageResponseDto> getReviewReport(Pageable pageable) {
        return reportRepository.findByReviewType(pageable);
    }

    public Page<ReportPageResponseDto> getReplyReport(Pageable pageable) {
        return reportRepository.findByReplyType(pageable);
    }

    public Page<ReportPageResponseDto> getStoreReport(Pageable pageable) {
        return reportRepository.findByStoreType(pageable);
    }

    public void createReport(Long memberId, CreateReportDto createReportDto) {
        Member member = memberService.getMember(memberId);
        Report report = createReportDto.toEntity(member);
        reportRepository.save(report);
    }
}
