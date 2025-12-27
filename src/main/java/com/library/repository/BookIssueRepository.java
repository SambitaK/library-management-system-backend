package com.library.repository;

import com.library.model.BookIssue;
import com.library.model.BookIssue.IssueStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface BookIssueRepository extends JpaRepository<BookIssue, Long> {

    List<BookIssue> findByMemberMemberId(Long memberId);

    List<BookIssue> findByBookBookId(Long bookId);

    List<BookIssue> findByStatus(IssueStatus status);

    @Query("SELECT bi FROM BookIssue bi WHERE bi.member.memberId = :memberId AND bi.status = 'ISSUED'")
    List<BookIssue> findActiveIssuesByMember(@Param("memberId") Long memberId);

    @Query("SELECT bi FROM BookIssue bi WHERE bi.status = 'ISSUED' AND bi.dueDate < :currentDate")
    List<BookIssue> findOverdueIssues(@Param("currentDate") LocalDate currentDate);

    @Query("SELECT COUNT(bi) FROM BookIssue bi WHERE bi.member.memberId = :memberId AND bi.status = 'ISSUED'")
    long countActiveIssuesByMember(@Param("memberId") Long memberId);

    @Query("SELECT COUNT(bi) > 0 FROM BookIssue bi WHERE " +
            "bi.book.bookId = :bookId AND bi.member.memberId = :memberId AND bi.status = 'ISSUED'")
    boolean isBookAlreadyIssuedToMember(@Param("bookId") Long bookId, @Param("memberId") Long memberId);
}