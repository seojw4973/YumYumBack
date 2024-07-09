package org.baratie.yumyum.domain.report.repository;

import org.baratie.yumyum.domain.report.domain.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long>, ReportCustomRepository {
}
