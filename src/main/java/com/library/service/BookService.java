package com.library.service;

import com.library.model.Book;
import com.library.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class BookService {

    private final BookRepository bookRepository;

    public List<Book> getAllBooks() {
        log.info("Fetching all books");
        return bookRepository.findAll();
    }

    public Book getBookById(Long id) {
        log.info("Fetching book with ID: {}", id);
        return bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Book not found with ID: " + id));
    }

    public Book getBookByIsbn(String isbn) {
        log.info("Fetching book with ISBN: {}", isbn);
        return bookRepository.findByIsbn(isbn)
                .orElseThrow(() -> new RuntimeException("Book not found with ISBN: " + isbn));
    }

    public List<Book> searchBooks(String keyword) {
        log.info("Searching books with keyword: {}", keyword);
        return bookRepository.searchBooks(keyword);
    }

    public List<Book> getAvailableBooks() {
        log.info("Fetching all available books");
        return bookRepository.findAllAvailableBooks();
    }

    public Book addBook(Book book) {
        log.info("Adding new book: {}", book.getTitle());

        if (bookRepository.existsByIsbn(book.getIsbn())) {
            throw new RuntimeException("Book with ISBN " + book.getIsbn() + " already exists");
        }

        return bookRepository.save(book);
    }

    public Book updateBook(Long id, Book bookDetails) {
        log.info("Updating book with ID: {}", id);

        Book book = getBookById(id);
        book.setIsbn(bookDetails.getIsbn());
        book.setTitle(bookDetails.getTitle());
        book.setAuthor(bookDetails.getAuthor());
        book.setPublisher(bookDetails.getPublisher());
        book.setPublicationYear(bookDetails.getPublicationYear());
        book.setCategory(bookDetails.getCategory());
        book.setTotalCopies(bookDetails.getTotalCopies());
        book.setAvailableCopies(bookDetails.getAvailableCopies());
        book.setShelfLocation(bookDetails.getShelfLocation());

        return bookRepository.save(book);
    }

    public void deleteBook(Long id) {
        log.info("Deleting book with ID: {}", id);
        Book book = getBookById(id);
        bookRepository.delete(book);
    }

    public Long getTotalBooksCount() {
        return bookRepository.count();
    }
}