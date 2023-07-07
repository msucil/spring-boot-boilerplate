package design.boilerplate.springboot.book.service;

import design.boilerplate.springboot.book.dto.BookRequest;
import design.boilerplate.springboot.book.dto.BookResponse;
import design.boilerplate.springboot.book.dto.BookUpdateRequest;
import design.boilerplate.springboot.book.mapper.BookMapper;
import design.boilerplate.springboot.model.Book;
import design.boilerplate.springboot.repository.BookRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookServiceImpl implements BookService {

    private final BookRepository repository;
    private final BookMapper bookMapper;

    public BookServiceImpl(BookRepository repository, BookMapper bookMapper) {
        this.repository = repository;
        this.bookMapper = bookMapper;
    }

    @Override
    public List<BookResponse> findAll() {
        return bookMapper.convertToBookResponses(repository.findAll());
    }

    @Override
    public Page<BookResponse> findAll(int page, int pageSize) {

       final Pageable pageable = Pageable.ofSize(pageSize).withPage(page - 1);

       Page<Book> books = repository.findAll(pageable);

        PageImpl<BookResponse> pageableResponse = new PageImpl<>(bookMapper.convertToBookResponses(books.stream().toList()), pageable, books.getTotalElements());

        return pageableResponse;
    }

    @Override
    public Optional<BookResponse> findOne(Long id) {
        return repository.findById(id).map(bookMapper::convertToBookResponse);
    }

    @Override
    public BookResponse save(BookRequest bookRequest) {
        final Book book = bookMapper.covertToBook(bookRequest);
        final Book savedBook = repository.save(book);

        return bookMapper.convertToBookResponse(savedBook);
    }

    @Override
    public BookResponse update(BookUpdateRequest bookRequest) {
        final Book book = bookMapper.covertToBook(bookRequest);
        final Book savedBook = repository.save(book);

        return bookMapper.convertToBookResponse(savedBook);
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }
}
