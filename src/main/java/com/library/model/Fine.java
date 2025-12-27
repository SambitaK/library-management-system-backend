package com.library.model;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "fines")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Fine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "fine_id")
    private Long fineId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "issue_id", nullable = false)
    private BookIssue bookIssue;

    @Column(name = "fine_amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal fineAmount;

    @Column(name = "fine_reason")
    private String fineReason;

    @Enumerated(EnumType.STRING)
    @Column(name = "paid_status", nullable = false)
    private PaidStatus paidStatus = PaidStatus.UNPAID;

    @Column(name = "payment_date")
    private LocalDate paymentDate;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    public enum PaidStatus {
        PAID, UNPAID
    }
}