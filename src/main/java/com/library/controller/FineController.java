package com.library.controller;

import com.library.model.Fine;
import com.library.service.FineService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/fines")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class FineController {

    private final FineService fineService;

    @GetMapping
    public ResponseEntity<List<Fine>> getAllFines() {
        return ResponseEntity.ok(fineService.getAllFines());
    }

    @GetMapping("/unpaid")
    public ResponseEntity<List<Fine>> getUnpaidFines() {
        return ResponseEntity.ok(fineService.getUnpaidFines());
    }

    @GetMapping("/member/{memberId}")
    public ResponseEntity<List<Fine>> getFinesByMember(@PathVariable Long memberId) {
        return ResponseEntity.ok(fineService.getFinesByMember(memberId));
    }

    @PutMapping("/{fineId}/pay")
    public ResponseEntity<Map<String, Object>> payFine(@PathVariable Long fineId) {
        try {
            Fine paidFine = fineService.payFine(fineId);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Fine paid successfully");
            response.put("fine", paidFine);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());

            return ResponseEntity.badRequest().body(response);
        }
    }
}