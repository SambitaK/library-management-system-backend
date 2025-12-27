package com.library.repository;

import com.library.model.Member;
import com.library.model.Member.MemberStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByStudentId(String studentId);

    Optional<Member> findByEmail(String email);

    List<Member> findByStatus(MemberStatus status);

    boolean existsByStudentId(String studentId);

    boolean existsByEmail(String email);

    @Query("SELECT m FROM Member m WHERE " +
            "LOWER(m.fullName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(m.studentId) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(m.email) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Member> searchMembers(@Param("keyword") String keyword);
}