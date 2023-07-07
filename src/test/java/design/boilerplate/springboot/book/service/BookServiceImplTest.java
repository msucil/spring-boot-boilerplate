package design.boilerplate.springboot.book.service;

import design.boilerplate.springboot.book.dto.BookRequest;
import design.boilerplate.springboot.book.dto.BookResponse;
import design.boilerplate.springboot.book.dto.BookUpdateRequest;
import design.boilerplate.springboot.book.mapper.BookMapper;
import design.boilerplate.springboot.repository.BookRepository;
import design.boilerplate.springboot.model.Book;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookServiceImplTest {

    @Mock
    private BookRepository repository;

    @Mock
    private BookMapper mapper;

    @InjectMocks
    private BookServiceImpl bookService;

    @Test
    public void testFindAllShouldReturnEmptyResults(){

        when(repository.findAll()).thenReturn(Collections.emptyList());

        final List<BookResponse> books = bookService.findAll();

        assertThat(books, empty());
    }

    @Test
    public void testFindAllShouldReturnResultsOfSize1(){
        Book saved = new Book();
        saved.setId(1L);
        saved.setTitle("book title");
        saved.setIsbn("isbn");
        saved.setAuthor("author");
        saved.setPublisher("publisher");
        saved.setPublishedDate(LocalDate.now());

        BookResponse book = new BookResponse();
        book.setId(1L);
        book.setTitle("book title");
        book.setIsbn("isbn");
        book.setAuthor("author");
        book.setPublisher("publisher");
        book.setPublishedDate(LocalDate.now());

        when(repository.findAll()).thenReturn(List.of(saved));
        when(mapper.convertToBookResponses(anyList())).thenReturn(List.of(book));

        final List<BookResponse> books = bookService.findAll();

        assertThat(books, hasSize(1));

        final BookResponse response = books.get(0);

        assertThat(response.getId(), equalTo(book.getId()));
        assertThat(response.getTitle(), equalTo(book.getTitle()));
        assertThat(response.getIsbn(), equalTo(book.getIsbn()));
        assertThat(response.getAuthor(), equalTo(book.getAuthor()));
        assertThat(response.getPublisher(), equalTo(book.getPublisher()));
        assertThat(response.getPublishedDate(), equalTo(book.getPublishedDate()));

        final ArgumentCaptor<List<Book>> bookCapture = ArgumentCaptor.forClass( (Class) List.class);

        verify(mapper, times(1)).convertToBookResponses(bookCapture.capture());

        final List<Book> mapperParam = bookCapture.getValue();

        assertThat(mapperParam, hasSize(1));
        assertThat(mapperParam.get(0).getAuthor(), equalTo(saved.getAuthor()));


    }

    @Test
    public void testFindOneShouldReturnEmpty(){
        when(repository.findById(anyLong())).thenReturn(Optional.empty());

        Optional<BookResponse> response = bookService.findOne(1L);

        assertThat(response.isEmpty(), equalTo(true));
    }

    @Test
    public void testFindOneShouldReturnNotEmpty(){
        Book saved = new Book();
        saved.setId(1L);
        saved.setTitle("book title");
        saved.setIsbn("isbn");
        saved.setAuthor("author");
        saved.setPublisher("publisher");
        saved.setPublishedDate(LocalDate.now());

        BookResponse book = new BookResponse();
        book.setId(1L);
        book.setTitle("book title");
        book.setIsbn("isbn");
        book.setAuthor("author");
        book.setPublisher("publisher");
        book.setPublishedDate(LocalDate.now());

        when(repository.findById(anyLong())).thenReturn(Optional.of(saved));
        when(mapper.convertToBookResponse(any(Book.class))).thenReturn(book);

        final Optional<BookResponse> bookResponse = bookService.findOne(1L);

        assertThat(bookResponse.isPresent(), equalTo(true));

        final BookResponse response = bookResponse.get();

        assertThat(response.getId(), equalTo(book.getId()));
        assertThat(response.getTitle(), equalTo(book.getTitle()));
        assertThat(response.getIsbn(), equalTo(book.getIsbn()));
        assertThat(response.getAuthor(), equalTo(book.getAuthor()));
        assertThat(response.getPublisher(), equalTo(book.getPublisher()));
        assertThat(response.getPublishedDate(), equalTo(book.getPublishedDate()));

        final ArgumentCaptor<Long> idCapture = ArgumentCaptor.forClass(Long.class);
        verify(repository, times(1)).findById(idCapture.capture());

        assertThat(idCapture.getValue(), equalTo(1L));

        final ArgumentCaptor<Book> bookCapture = ArgumentCaptor.forClass( Book.class);

        verify(mapper, times(1)).convertToBookResponse(bookCapture.capture());

        final Book mapperParam = bookCapture.getValue();

        assertThat(mapperParam, notNullValue());
        assertThat(mapperParam.getAuthor(), equalTo(saved.getAuthor()));
    }

    @Test
    public void testSaveShouldSaveBook(){
        BookRequest bookRequest = new BookRequest();
        bookRequest.setTitle("book title");
        bookRequest.setIsbn("isbn");
        bookRequest.setAuthor("author");
        bookRequest.setPublisher("publisher");
        bookRequest.setPublishedDate(LocalDate.now());

        Book book = new Book();
        book.setTitle("book title");
        book.setIsbn("isbn");
        book.setAuthor("author");
        book.setPublisher("publisher");
        book.setPublishedDate(LocalDate.now());

        Book saved = new Book();
        saved.setId(12L);
        saved.setTitle("book title");
        saved.setIsbn("isbn");
        saved.setAuthor("author");
        saved.setPublisher("publisher");
        saved.setPublishedDate(LocalDate.now());

        BookResponse response = new BookResponse();
        response.setId(12L);
        response.setTitle("book title");
        response.setIsbn("isbn");
        response.setAuthor("author");
        response.setPublisher("publisher");
        response.setPublishedDate(LocalDate.now());

        when(mapper.covertToBook(any(BookRequest.class))).thenReturn(book);
        when(repository.save(any(Book.class))).thenReturn(saved);
        when(mapper.convertToBookResponse(any(Book.class))).thenReturn(response);

        BookResponse savedResponse = bookService.save(bookRequest);

        assertThat(savedResponse, notNullValue());
        assertThat(savedResponse.getId(), equalTo(saved.getId()));
        assertThat(savedResponse.getTitle(), equalTo(saved.getTitle()));
        assertThat(savedResponse.getAuthor(), equalTo(saved.getAuthor()));
        assertThat(savedResponse.getPublisher(), equalTo(saved.getPublisher()));
        assertThat(savedResponse.getPublishedDate(), equalTo(saved.getPublishedDate()));

        ArgumentCaptor<Book> bookCaptor = ArgumentCaptor.forClass(Book.class);
        verify(repository, times(1)).save(bookCaptor.capture());

        assertThat(bookCaptor.getValue(), notNullValue());
        assertThat(bookCaptor.getValue().getId(), nullValue());
        assertThat(bookCaptor.getValue().getTitle(), equalTo(book.getTitle()));
    }

    @Test
    public void testUpdateShouldSaveBook(){
        BookUpdateRequest bookRequest = new BookUpdateRequest();
        bookRequest.setId(100L);
        bookRequest.setTitle("book title");
        bookRequest.setIsbn("isbn");
        bookRequest.setAuthor("author");
        bookRequest.setPublisher("publisher");
        bookRequest.setPublishedDate(LocalDate.now());

        Book book = new Book();
        book.setId(100L);
        book.setTitle("book title");
        book.setIsbn("isbn");
        book.setAuthor("author");
        book.setPublisher("publisher");
        book.setPublishedDate(LocalDate.now());

        Book saved = new Book();
        saved.setId(100L);
        saved.setTitle("book title");
        saved.setIsbn("isbn");
        saved.setAuthor("author");
        saved.setPublisher("publisher");
        saved.setPublishedDate(LocalDate.now());

        BookResponse response = new BookResponse();
        response.setId(100L);
        response.setTitle("book title");
        response.setIsbn("isbn");
        response.setAuthor("author");
        response.setPublisher("publisher");
        response.setPublishedDate(LocalDate.now());

        when(mapper.covertToBook(any(BookUpdateRequest.class))).thenReturn(book);
        when(repository.save(any(Book.class))).thenReturn(saved);
        when(mapper.convertToBookResponse(any(Book.class))).thenReturn(response);

        BookResponse savedResponse = bookService.update(bookRequest);

        assertThat(savedResponse, notNullValue());
        assertThat(savedResponse.getId(), equalTo(saved.getId()));
        assertThat(savedResponse.getTitle(), equalTo(saved.getTitle()));
        assertThat(savedResponse.getAuthor(), equalTo(saved.getAuthor()));
        assertThat(savedResponse.getPublisher(), equalTo(saved.getPublisher()));
        assertThat(savedResponse.getPublishedDate(), equalTo(saved.getPublishedDate()));

        ArgumentCaptor<Book> bookCaptor = ArgumentCaptor.forClass(Book.class);
        verify(repository, times(1)).save(bookCaptor.capture());

        assertThat(bookCaptor.getValue(), notNullValue());
        assertThat(bookCaptor.getValue().getId(), notNullValue());
        assertThat(bookCaptor.getValue().getId(), equalTo(book.getId()));
        assertThat(bookCaptor.getValue().getTitle(), equalTo(book.getTitle()));
    }

    @Test
    public void testDeleteShouldDeleteBookById(){
        doNothing().when(repository).deleteById(anyLong());

        bookService.delete(11L);

        final ArgumentCaptor<Long> idCaptor = ArgumentCaptor.forClass(Long.class);

        verify(repository, times(1)).deleteById(idCaptor.capture());

        assertThat(idCaptor.getValue(), notNullValue());
        assertThat(idCaptor.getValue(), equalTo(11L));
    }


}