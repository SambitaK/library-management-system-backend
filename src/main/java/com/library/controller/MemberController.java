package com.library.controller;

import com.library.model.Member;
import com.library.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class MemberController {

    private final MemberService memberService;

    @GetMapping
    public ResponseEntity<List<Member>> getAllMembers() {
        return ResponseEntity.ok(memberService.getAllMembers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Member> getMemberById(@PathVariable Long id) {
        return ResponseEntity.ok(memberService.getMemberById(id));
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<Member> getMemberByStudentId(@PathVariable String studentId) {
        return ResponseEntity.ok(memberService.getMemberByStudentId(studentId));
    }

    @GetMapping("/search")
    public ResponseEntity<List<Member>> searchMembers(@RequestParam String query) {
        return ResponseEntity.ok(memberService.searchMembers(query));
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> addMember(@Valid @RequestBody Member member) {
        try {
            Member savedMember = memberService.addMember(member);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Member added successfully");
            response.put("member", savedMember);

            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());

            return ResponseEntity.badRequest().body(response);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateMember(
            @PathVariable Long id,
            @Valid @RequestBody Member member) {
        try {
            Member updatedMember = memberService.updateMember(id, member);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Member updated successfully");
            response.put("member", updatedMember);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());

            return ResponseEntity.badRequest().body(response);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteMember(@PathVariable Long id) {
        try {
            memberService.deleteMember(id);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Member deleted successfully");

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());

            return ResponseEntity.badRequest().body(response);
        }
    }
}