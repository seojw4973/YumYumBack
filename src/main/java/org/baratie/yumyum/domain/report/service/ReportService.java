package org.baratie.yumyum.domain.report.service;

import lombok.RequiredArgsConstructor;
import org.baratie.yumyum.domain.member.domain.Member;
import org.baratie.yumyum.domain.member.service.MemberService;
import org.baratie.yumyum.domain.report.domain.Report;
import org.baratie.yumyum.domain.report.dto.CreateReportDto;
import org.baratie.yumyum.domain.report.dto.ReportPageResponseDto;
import org.baratie.yumyum.domain.report.repository.ReportRepository;
import org.baratie.yumyum.global.utils.pageDto.CustomPageDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReportService {
    private final ReportRepository reportRepository;
    private final MemberService memberService;

    /**
     * 신고된 리뷰
     */
    public CustomPageDto getReviewReport(Pageable pageable) {
        Page<ReportPageResponseDto> reportReview = reportRepository.findByReviewType(pageable);

        return new CustomPageDto(reportReview);
    }

    /**
     * 신고된 댓글
     */
    public CustomPageDto getReplyReport(Pageable pageable) {
        Page<ReportPageResponseDto> reportReply = reportRepository.findByReplyType(pageable);

        return new CustomPageDto(reportReply);
    }

    /**
     * 신고된 가게
     */
    public CustomPageDto getStoreReport(Pageable pageable) {
        Page<ReportPageResponseDto> reportPage = reportRepository.findByStoreType(pageable);

        return new CustomPageDto(reportPage);
    }

    /**
     * 신고하기
     * @param memberId 신고자
     * @param createReportDto 신고 내용
     */
    public void createReport(Long memberId, CreateReportDto createReportDto) {
        Member member = memberService.getMember(memberId);
        Report report = createReportDto.toEntity(member);

        reportRepository.save(report);
    }
}
