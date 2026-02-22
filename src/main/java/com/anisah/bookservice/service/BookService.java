package com.anisah.bookservice.service;

import com.anisah.bookservice.dto.BookRequest;
import com.anisah.bookservice.dto.BookResponse;
import com.anisah.bookservice.entity.Book;
import com.anisah.bookservice.exception.ResourceNotFoundException;
import com.anisah.bookservice.repository.BookRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookService {
    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public BookResponse create(BookRequest request) {
        Book book = mapToEntity(request);
        return mapToResponse(bookRepository.save(book));
    }

    public List<BookResponse> findAll() {
        return bookRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public BookResponse findById(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + id));
        return mapToResponse(book);
    }

    public BookResponse update(Long id, BookRequest request) {
        Book existing = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + id));

        existing.setTitle(request.getTitle());
        existing.setAuthor(request.getAuthor());
        existing.setIsbn(request.getIsbn());
        existing.setPublishedDate(request.getPublishedDate());

        return mapToResponse(bookRepository.save(existing));
    }

    public BookResponse partialUpdate(Long id, BookRequest request) {
        Book existing = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + id));

        if (request.getTitle() != null) {
            existing.setTitle(request.getTitle());
        }

        if (request.getAuthor() != null) {
            existing.setAuthor(request.getAuthor());
        }

        if (request.getIsbn() != null) {
            existing.setIsbn(request.getIsbn());
        }

        if (request.getPublishedDate() != null) {
            existing.setPublishedDate(request.getPublishedDate());
        }

        return mapToResponse(bookRepository.save(existing));
    }

    public void delete(Long id) {
        Book existing = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + id));
        bookRepository.delete(existing);
    }

    // ===== Mapper =====
    private Book mapToEntity(BookRequest request) {
        Book book = new Book();
        book.setTitle(request.getTitle());
        book.setAuthor(request.getAuthor());
        book.setIsbn(request.getIsbn());
        book.setPublishedDate(request.getPublishedDate());
        return book;
    }

    private BookResponse mapToResponse(Book book) {
        return new BookResponse(
                book.getId(),
                book.getTitle(),
                book.getAuthor(),
                book.getIsbn(),
                book.getPublishedDate()
        );
    }
}