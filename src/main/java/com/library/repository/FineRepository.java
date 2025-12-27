package com.library.repository;

import com.library.model.Fine;
import com.library.model.Fine.PaidStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.math.BigDecimal;
import java.util.List;

@Repository
public interface FineRepository extends JpaRepository<Fine, Long> {

    List<Fine> findByPaidStatus(PaidStatus status);

    List<Fine> findByBookIssueIssueId(Long issueId);

    @Query("SELECT f FROM Fine f WHERE f.bookIssue.member.memberId = :memberId AND f.paidStatus = 'UNPAID'")
    List<Fine> findUnpaidFinesByMember(@Param("memberId") Long memberId);

    @Query("SELECT COALESCE(SUM(f.fineAmount), 0) FROM Fine f WHERE f.paidStatus = 'UNPAID'")
    BigDecimal calculateTotalUnpaidFines();

    @Query("SELECT COALESCE(SUM(f.fineAmount), 0) FROM Fine f WHERE f.paidStatus = 'PAID'")
    BigDecimal calculateTotalPaidFines();

    @Query("SELECT f FROM Fine f WHERE f.bookIssue.member.memberId = :memberId ORDER BY f.createdAt DESC")
    List<Fine> findAllFinesByMember(@Param("memberId") Long memberId);
}