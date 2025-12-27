package com.library.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "books")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "book_id")
    private Long bookId;

    @Column(unique = true, nullable = false, length = 20)
    @NotBlank(message = "ISBN is required")
    private String isbn;

    @Column(nullable = false)
    @NotBlank(message = "Title is required")
    private String title;

    @Column(nullable = false, length = 100)
    @NotBlank(message = "Author is required")
    private String author;

    @Column(length = 100)
    private String publisher;

    @Column(name = "publication_year")
    private Integer publicationYear;

    @Column(length = 50)
    private String category;

    @Column(name = "total_copies", nullable = false)
    @Min(value = 0, message = "Total copies must be zero or positive")
    private Integer totalCopies = 1;

    @Column(name = "available_copies", nullable = false)
    @Min(value = 0, message = "Available copies must be zero or positive")
    private Integer availableCopies = 1;

    @Column(name = "shelf_location", length = 50)
    private String shelfLocation;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public boolean isAvailable() {
        return this.availableCopies != null && this.availableCopies > 0;
    }

    public void decrementAvailableCopies() {
        if (this.availableCopies > 0) {
            this.availableCopies--;
        }
    }

    public void incrementAvailableCopies() {
        if (this.availableCopies < this.totalCopies) {
            this.availableCopies++;
        }
    }
}
