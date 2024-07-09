package org.baratie.yumyum.domain.report.service;

import lombok.RequiredArgsConstructor;
import org.baratie.yumyum.domain.report.dto.ReportPageResponseDto;
import org.baratie.yumyum.domain.report.repository.ReportRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReportService {
    private final ReportRepository reportRepository;

    public Page<ReportPageResponseDto> getReviewReport(Pageable pageable) {
        return reportRepository.findByReviewType(pageable);
    }


    public Page<ReportPageResponseDto> getReplyReport(Pageable pageable) {
        return reportRepository.findByReplyType(pageable);
    }
}
