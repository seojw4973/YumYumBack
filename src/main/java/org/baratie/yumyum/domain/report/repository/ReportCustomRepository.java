package org.baratie.yumyum.domain.report.repository;

import org.baratie.yumyum.domain.report.dto.ReportPageResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface ReportCustomRepository {

    Page<ReportPageResponseDto> findByReviewType(Pageable pageable);

    Page<ReportPageResponseDto> findByReplyType(Pageable pageable);
}
