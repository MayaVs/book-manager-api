package com.techreturners.bookmanager.controller;

import com.techreturners.bookmanager.model.Book;
import com.techreturners.bookmanager.service.BookManagerService;
import lombok.val;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/book")
public class BookManagerController {

    BookManagerService bookManagerService;

    public BookManagerController(BookManagerService bookManagerService) {
        this.bookManagerService = bookManagerService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<Book>> getAllBooks() {
        List<Book> books = bookManagerService.getAllBooks();
        return new ResponseEntity<>(books, HttpStatus.OK);
    }

    @GetMapping("/{bookId}")
    public ResponseEntity<Book> getBookById(@PathVariable Long bookId) {
        Book findBook = bookManagerService.getBookById(bookId);
        if(findBook != null)
            return new ResponseEntity<>(findBook, HttpStatus.OK);
        else {
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.add("book", "/api/v1/" + bookId + " doesn't exists ");

            return new ResponseEntity<>(null, httpHeaders, HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    public ResponseEntity<Book> addBook(@RequestBody Book book) {
        ResponseEntity<Book> findBook = getBookById(book.getId());
        if(findBook.getStatusCode() != HttpStatus.NOT_FOUND){
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.add("book", "/api/v1/book/" + book.getId().toString() + "already exists ");
            return new ResponseEntity<>(book, httpHeaders, HttpStatus.NOT_ACCEPTABLE);
        }
        Book newBook = bookManagerService.insertBook(book);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("book", "/api/v1/book/" + newBook.getId().toString());
        return new ResponseEntity<>(newBook, httpHeaders, HttpStatus.CREATED);
    }

    //User Story 4 - Update Book By Id Solution
    @PutMapping({"/{bookId}"})
    public ResponseEntity<Book> updateBookById(@PathVariable("bookId") Long bookId, @RequestBody Book book) {
        bookManagerService.updateBookById(bookId, book);
        return new ResponseEntity<>(bookManagerService.getBookById(bookId), HttpStatus.OK);
    }

    //User Story 5 - Delete Book By Id Solution
    @DeleteMapping({"/{bookId}"})
    public ResponseEntity deleteBookById(@PathVariable("bookId") Long bookId) {
        bookManagerService.deleteBookById(bookId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
