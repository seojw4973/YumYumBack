package org.baratie.yumyum.domain.report.controller;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.baratie.yumyum.domain.report.dto.ReportPageResponseDto;
import org.baratie.yumyum.domain.report.service.ReportService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/report")
public class ReportController {

    private final ReportService reportService;

    @GetMapping("/review")
    public ResponseEntity<Page<ReportPageResponseDto>> findReviewReport(@PageableDefault(size=5) Pageable pageable) {
        Page<ReportPageResponseDto> reportReviewList = reportService.getReviewReport(pageable);
        return ResponseEntity.status(HttpStatus.OK).body(reportReviewList);
    }

    @GetMapping("/reply")
    public ResponseEntity<Page<ReportPageResponseDto>> findReplyReport(@PageableDefault(size=5) Pageable pageable) {
        Page<ReportPageResponseDto> reportReplyList = reportService.getReplyReport(pageable);
        return ResponseEntity.status(HttpStatus.OK).body(reportReplyList);
    }


}
