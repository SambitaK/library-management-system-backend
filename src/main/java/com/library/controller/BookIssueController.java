package com.library.controller;

import com.library.model.BookIssue;
import com.library.service.BookIssueService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/issues")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class BookIssueController {

    private final BookIssueService issueService;

    @GetMapping
    public ResponseEntity<List<BookIssue>> getAllIssues() {
        return ResponseEntity.ok(issueService.getAllIssues());
    }

    @GetMapping("/member/{memberId}")
    public ResponseEntity<List<BookIssue>> getIssuesByMember(@PathVariable Long memberId) {
        return ResponseEntity.ok(issueService.getIssuesByMember(memberId));
    }

    @GetMapping("/overdue")
    public ResponseEntity<List<BookIssue>> getOverdueIssues() {
        return ResponseEntity.ok(issueService.getOverdueIssues());
    }

    @PostMapping("/issue")
    public ResponseEntity<Map<String, Object>> issueBook(
            @RequestParam Long bookId,
            @RequestParam Long memberId) {
        try {
            BookIssue issue = issueService.issueBook(bookId, memberId);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Book issued successfully");
            response.put("issue", issue);

            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());

            return ResponseEntity.badRequest().body(response);
        }
    }

    @PutMapping("/return/{issueId}")
    public ResponseEntity<Map<String, Object>> returnBook(@PathVariable Long issueId) {
        try {
            BookIssue issue = issueService.returnBook(issueId);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Book returned successfully");
            response.put("issue", issue);
            response.put("fine", issue.getFineAmount());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());

            return ResponseEntity.badRequest().body(response);
        }
    }
}