package com.library.service;

import com.library.model.Member;
import com.library.model.Member.MemberStatus;
import com.library.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class MemberService {

    private final MemberRepository memberRepository;

    public List<Member> getAllMembers() {
        log.info("Fetching all members");
        return memberRepository.findAll();
    }

    public Member getMemberById(Long id) {
        log.info("Fetching member with ID: {}", id);
        return memberRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Member not found with ID: " + id));
    }

    public Member getMemberByStudentId(String studentId) {
        log.info("Fetching member with student ID: {}", studentId);
        return memberRepository.findByStudentId(studentId)
                .orElseThrow(() -> new RuntimeException("Member not found with student ID: " + studentId));
    }

    public List<Member> searchMembers(String keyword) {
        log.info("Searching members with keyword: {}", keyword);
        return memberRepository.searchMembers(keyword);
    }

    public Member addMember(Member member) {
        log.info("Adding new member: {}", member.getFullName());

        if (memberRepository.existsByStudentId(member.getStudentId())) {
            throw new RuntimeException("Member with student ID " + member.getStudentId() + " already exists");
        }

        if (memberRepository.existsByEmail(member.getEmail())) {
            throw new RuntimeException("Member with email " + member.getEmail() + " already exists");
        }

        return memberRepository.save(member);
    }

    public Member updateMember(Long id, Member memberDetails) {
        log.info("Updating member with ID: {}", id);

        Member member = getMemberById(id);
        member.setFullName(memberDetails.getFullName());
        member.setEmail(memberDetails.getEmail());
        member.setPhone(memberDetails.getPhone());
        member.setAddress(memberDetails.getAddress());
        member.setStatus(memberDetails.getStatus());

        return memberRepository.save(member);
    }

    public void deleteMember(Long id) {
        log.info("Deleting member with ID: {}", id);
        Member member = getMemberById(id);
        memberRepository.delete(member);
    }

    public long getTotalMembersCount() {
        return memberRepository.count();
    }
}