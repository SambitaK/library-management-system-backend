package com.library.service;

import com.library.model.*;
import com.library.repository.BookIssueRepository;
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
public class BookIssueService {

    private final BookIssueRepository issueRepository;
    private final BookService bookService;
    private final MemberService memberService;
    private final FineService fineService;

    private static final int LOAN_PERIOD_DAYS = 14;
    private static final BigDecimal FINE_PER_DAY = new BigDecimal("5.00");
    private static final int MAX_BOOKS_PER_MEMBER = 5;

    public BookIssue issueBook(Long bookId, Long memberId) {
        log.info("Issuing book ID: {} to member ID: {}", bookId, memberId);

        Book book = bookService.getBookById(bookId);
        Member member = memberService.getMemberById(memberId);

        if (!book.isAvailable()) {
            throw new RuntimeException("Book is not available");
        }

        if (member.getStatus() != Member.MemberStatus.ACTIVE) {
            throw new RuntimeException("Member is not active");
        }

        if (issueRepository.isBookAlreadyIssuedToMember(bookId, memberId)) {
            throw new RuntimeException("Member already has this book");
        }

        long activeIssues = issueRepository.countActiveIssuesByMember(memberId);
        if (activeIssues >= MAX_BOOKS_PER_MEMBER) {
            throw new RuntimeException("Member has reached maximum books limit");
        }

        BookIssue issue = BookIssue.builder()
                .book(book)
                .member(member)
                .issueDate(LocalDate.now())
                .dueDate(LocalDate.now().plusDays(LOAN_PERIOD_DAYS))
                .status(BookIssue.IssueStatus.ISSUED)
                .fineAmount(BigDecimal.ZERO)
                .build();

        book.decrementAvailableCopies();
        bookService.updateBook(bookId, book);

        BookIssue savedIssue = issueRepository.save(issue);
        log.info("Book issued successfully. Issue ID: {}", savedIssue.getIssueId());

        return savedIssue;
    }

    public BookIssue returnBook(Long issueId) {
        log.info("Processing return for issue ID: {}", issueId);

        BookIssue issue = issueRepository.findById(issueId)
                .orElseThrow(() -> new RuntimeException("Issue not found"));

        if (issue.getStatus() == BookIssue.IssueStatus.RETURNED) {
            throw new RuntimeException("Book already returned");
        }

        issue.setReturnDate(LocalDate.now());
        issue.setStatus(BookIssue.IssueStatus.RETURNED);

        if (LocalDate.now().isAfter(issue.getDueDate())) {
            long daysOverdue = LocalDate.now().toEpochDay() - issue.getDueDate().toEpochDay();
            BigDecimal fineAmount = FINE_PER_DAY.multiply(BigDecimal.valueOf(daysOverdue));
            issue.setFineAmount(fineAmount);

            fineService.createFine(issue, fineAmount, "Late return - " + daysOverdue + " days overdue");
            log.info("Book returned with fine: ₹{}", fineAmount);
        }

        Book book = issue.getBook();
        book.incrementAvailableCopies();
        bookService.updateBook(book.getBookId(), book);

        return issueRepository.save(issue);
    }

    public List<BookIssue> getAllIssues() {
        return issueRepository.findAll();
    }

    public List<BookIssue> getIssuesByMember(Long memberId) {
        return issueRepository.findByMemberMemberId(memberId);
    }

    public List<BookIssue> getOverdueIssues() {
        return issueRepository.findOverdueIssues(LocalDate.now());
    }
}