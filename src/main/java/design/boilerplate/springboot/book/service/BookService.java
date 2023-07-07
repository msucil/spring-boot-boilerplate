package design.boilerplate.springboot.book.service;

import design.boilerplate.springboot.book.dto.BookRequest;
import design.boilerplate.springboot.book.dto.BookResponse;
import design.boilerplate.springboot.book.dto.BookUpdateRequest;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface BookService {

    List<BookResponse> findAll();

    Page<BookResponse> findAll(int page, int pageSize);

    Optional<BookResponse> findOne(Long id);

    BookResponse save(BookRequest bookRequest);

    BookResponse update(BookUpdateRequest bookRequest);

    void delete(Long id);
}
