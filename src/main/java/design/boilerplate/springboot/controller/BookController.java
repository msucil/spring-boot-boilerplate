package design.boilerplate.springboot.controller;

import design.boilerplate.springboot.book.dto.BookRequest;
import design.boilerplate.springboot.book.dto.BookResponse;
import design.boilerplate.springboot.book.dto.BookUpdateRequest;
import design.boilerplate.springboot.book.service.BookService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/books")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping
    public ResponseEntity<List<BookResponse>> findAll() {
        return ResponseEntity.ok(bookService.findAll());
    }

    @PostMapping
    public ResponseEntity<BookResponse> create(@RequestBody @Valid BookRequest bookRequest) {

        return ResponseEntity.ok(bookService.save(bookRequest));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookResponse> findOne(@PathVariable("id") Long id) {
        Optional<BookResponse> book = bookService.findOne(id);

        return book.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());

    }

    @PutMapping("/{id}")
    public ResponseEntity<BookResponse> update(@PathVariable("id") Long id, @RequestBody @Valid BookUpdateRequest bookRequest) {
        Optional<BookResponse> response  = bookService.findOne(id);
        if(response.isEmpty()){
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Book with given ID does not exist");
        }

        return ResponseEntity.ok(bookService.update(bookRequest));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable("id") Long id) {
        Optional<BookResponse> response  = bookService.findOne(id);
        if(response.isEmpty()){
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Book with given ID does not exist");
        }

        bookService.delete(id);

        return ResponseEntity.ok("successful");
    }

    @GetMapping("/paginate")
    @RolesAllowed(value = {"PLATFORM_ADMIN", "SUPER_ADMIN", "ADMIN"})
    public ResponseEntity<Page<BookResponse>> getPagination(@RequestParam(value = "page", defaultValue = "1") int page,
                                              @RequestParam(value = "limit", defaultValue = "10") int size) {
        return ResponseEntity.ok(bookService.findAll(page, size));
    }
}
