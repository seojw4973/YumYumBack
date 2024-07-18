package org.baratie.yumyum.domain.report.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.baratie.yumyum.domain.member.domain.CustomUserDetails;
import org.baratie.yumyum.domain.report.dto.CreateReportDto;
import org.baratie.yumyum.domain.report.service.ReportService;
import org.baratie.yumyum.global.utils.pageDto.CustomPageDto;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/report")
public class ReportController {

    private final ReportService reportService;

    /**
     * 신고된 리뷰 조회
     */
    @GetMapping("/review")
    public ResponseEntity<CustomPageDto> findReviewReport(Pageable pageable) {
        CustomPageDto reportReviewPage = reportService.getReviewReport(pageable);

        return ResponseEntity.status(HttpStatus.OK).body(reportReviewPage);
    }

    /**
     * 신고된 댓글 조회
     */
    @GetMapping("/reply")
    public ResponseEntity<CustomPageDto> findReplyReport(Pageable pageable) {
        CustomPageDto reportReplyPage = reportService.getReplyReport(pageable);

        return ResponseEntity.status(HttpStatus.OK).body(reportReplyPage);
    }

    /**
     * 신고된 가게 조회
     */
    @GetMapping("/store")
    public ResponseEntity<CustomPageDto> findStoreReport(Pageable pageable) {
        CustomPageDto storeReport = reportService.getStoreReport(pageable);

        return ResponseEntity.status(HttpStatus.OK).body(storeReport);
    }

    /**
     * 신고하기
     */
    @PostMapping
    public ResponseEntity<Void> createReport(@AuthenticationPrincipal CustomUserDetails customUserDetails, @Valid @RequestBody CreateReportDto createReportDto) {
        Long memberId = customUserDetails.getId();
        reportService.createReport(memberId, createReportDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
