package com.library.service;

import com.library.model.BookIssue;
import com.library.model.Fine;
import com.library.model.Fine.PaidStatus;
import com.library.repository.FineRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class FineService {

    private final FineRepository fineRepository;

    public Fine createFine(BookIssue bookIssue, BigDecimal amount, String reason) {
        log.info("Creating fine for issue ID: {}, Amount: ₹{}", bookIssue.getIssueId(), amount);

        Fine fine = Fine.builder()
                .bookIssue(bookIssue)
                .fineAmount(amount)
                .fineReason(reason)
                .paidStatus(PaidStatus.UNPAID)
                .build();

        return fineRepository.save(fine);
    }

    public List<Fine> getAllFines() {
        log.info("Fetching all fines");
        return fineRepository.findAll();
    }

    public List<Fine> getUnpaidFines() {
        log.info("Fetching unpaid fines");
        return fineRepository.findByPaidStatus(PaidStatus.UNPAID);
    }

    public List<Fine> getFinesByMember(Long memberId) {
        log.info("Fetching fines for member ID: {}", memberId);
        return fineRepository.findAllFinesByMember(memberId);
    }

    public Fine payFine(Long fineId) {
        log.info("Processing payment for fine ID: {}", fineId);

        Fine fine = fineRepository.findById(fineId)
                .orElseThrow(() -> new RuntimeException("Fine not found with ID: " + fineId));

        if (fine.getPaidStatus() == PaidStatus.PAID) {
            throw new RuntimeException("Fine already paid");
        }

        fine.setPaidStatus(PaidStatus.PAID);
        fine.setPaymentDate(LocalDate.now());

        return fineRepository.save(fine);
    }

    public BigDecimal getTotalUnpaidFines() {
        return fineRepository.calculateTotalUnpaidFines();
    }
}