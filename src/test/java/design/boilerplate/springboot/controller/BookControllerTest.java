package design.boilerplate.springboot.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import design.boilerplate.springboot.book.dto.BookRequest;
import design.boilerplate.springboot.book.dto.BookResponse;
import design.boilerplate.springboot.book.dto.BookUpdateRequest;
import design.boilerplate.springboot.book.service.BookService;
import design.boilerplate.springboot.configuration.SecurityConfiguration;
import design.boilerplate.springboot.configuration.TestSecurityConfiguration;
import design.boilerplate.springboot.security.service.UserDetailsServiceImpl;
import design.boilerplate.springboot.security.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(value = BookController.class)
@Import(value = {TestSecurityConfiguration.class,  SecurityConfiguration.class})
class BookControllerTest {

    private static final String API_PREFIX = "/api/v1/books";

    @MockBean
    UserDetailsServiceImpl userDetailsService;

    @MockBean
    UserService userService;

    @MockBean
    private BookService bookService;

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();

    @Test
    public void testFindAllShouldReturnEmptyResult() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(API_PREFIX).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty())
                .andDo(print());
    }

    @Test
    public void testFindAllShouldReturnValues() throws Exception {
        BookResponse book1 = new BookResponse();
        book1.setId(1L);
        book1.setTitle("book2");

        BookResponse book2 = new BookResponse();
        book2.setId(2L);
        book2.setTitle("book2");

        when(bookService.findAll()).thenReturn(List.of(book1, book2));

        mockMvc.perform(MockMvcRequestBuilders.get(API_PREFIX).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.[0].id").value(book1.getId()))
                .andExpect(jsonPath("$.[0].title").value(book1.getTitle()))
                .andExpect(jsonPath("$.[1].id").value(book2.getId()))
                .andExpect(jsonPath("$.[1].title").value(book2.getTitle()))
                .andDo(print());
    }

    @Test
    public void testCreateShouldSendBadRequest() throws Exception {
        BookRequest request = new BookRequest();

        mockMvc.perform(MockMvcRequestBuilders.post(API_PREFIX).contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.message").isArray())
                .andDo(print());
    }

    @Test
    public void testCreateShouldSaveNewBook() throws Exception {
        BookRequest request = new BookRequest();
        request.setTitle("bookTitle");
        request.setIsbn("isbn");
        request.setAuthor("author");
        request.setPublishedDate(LocalDate.now());
        request.setPublisher("publisher");

        BookResponse response = new BookResponse();
        response.setId(1L);
        response.setIsbn(request.getIsbn());
        response.setTitle(request.getTitle());
        response.setAuthor(request.getAuthor());
        response.setPublisher(request.getPublisher());
        response.setPublishedDate(request.getPublishedDate());


        when(bookService.save(request)).thenReturn(response);

        mockMvc.perform(MockMvcRequestBuilders.post(API_PREFIX).contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(response.getId()))
                .andExpect(jsonPath("$.title").value(request.getTitle()))
                .andExpect(jsonPath("$.isbn").value(request.getIsbn()))
                .andExpect(jsonPath("$.author").value(request.getAuthor()))
                .andExpect(jsonPath("$.publishedDate").value(request.getPublishedDate().toString()))
                .andExpect(jsonPath("$.publisher").value(request.getPublisher()));
    }


    @Test
    public void testUpdateShouldUpdateBook() throws Exception {
        BookUpdateRequest request = new BookUpdateRequest();
        request.setId(1L);
        request.setTitle("bookTitle");
        request.setIsbn("isbn");
        request.setAuthor("author");
        request.setPublishedDate(LocalDate.now());
        request.setPublisher("publisher");

        BookResponse response = new BookResponse();
        response.setId(request.getId());
        response.setIsbn(request.getIsbn());
        response.setTitle(request.getTitle());
        response.setAuthor(request.getAuthor());
        response.setPublisher(request.getPublisher());
        response.setPublishedDate(request.getPublishedDate());


        when(bookService.findOne(anyLong())).thenReturn(Optional.of(new BookResponse()));
        when(bookService.update(request)).thenReturn(response);

        mockMvc.perform(MockMvcRequestBuilders.put(API_PREFIX+"/1").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(response.getId()))
                .andExpect(jsonPath("$.title").value(request.getTitle()))
                .andExpect(jsonPath("$.isbn").value(request.getIsbn()))
                .andExpect(jsonPath("$.author").value(request.getAuthor()))
                .andExpect(jsonPath("$.publishedDate").value(request.getPublishedDate().toString()))
                .andExpect(jsonPath("$.publisher").value(request.getPublisher()));
    }

    @Test
    public void testUpdateShouldThrowBadRequest() throws Exception {
        BookUpdateRequest request = new BookUpdateRequest();
        request.setId(1L);
        request.setTitle("bookTitle");
        request.setIsbn("isbn");
        request.setAuthor("author");
        request.setPublishedDate(LocalDate.now());
        request.setPublisher("publisher");



        when(bookService.findOne(1L)).thenReturn(Optional.ofNullable(null));

        mockMvc.perform(MockMvcRequestBuilders.put(API_PREFIX+"/1").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.path").value(API_PREFIX + "/1"))
                .andExpect(jsonPath("$.title").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("Book with given ID does not exist"))
                .andExpect(jsonPath("$.status").value(400))
                .andDo(print());
    }

    @Test
    public void testUpdateShouldThrowBadRequestOnEmptyId() throws Exception {
        BookUpdateRequest request = new BookUpdateRequest();
        request.setTitle("bookTitle");
        request.setIsbn("isbn");
        request.setAuthor("author");
        request.setPublishedDate(LocalDate.now());
        request.setPublisher("publisher");

        BookResponse response = new BookResponse();
        response.setId(request.getId());
        response.setIsbn(request.getIsbn());
        response.setTitle(request.getTitle());
        response.setAuthor(request.getAuthor());
        response.setPublisher(request.getPublisher());
        response.setPublishedDate(request.getPublishedDate());


        when(bookService.update(request)).thenReturn(response);

        mockMvc.perform(MockMvcRequestBuilders.put(API_PREFIX+"/1").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.message").isArray())
                .andDo(print());
    }

    @Test
    public void testUpdateShouldDeleteRecord() throws Exception {

        ArgumentCaptor<Long> idCaptor = ArgumentCaptor.forClass(Long.class);

        when(bookService.findOne(anyLong())).thenReturn(Optional.of(new BookResponse()));

        doNothing().when(bookService).delete(anyLong());

        mockMvc.perform(MockMvcRequestBuilders.delete(API_PREFIX+"/1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(bookService, times(1)).delete(idCaptor.capture());

        assertThat(idCaptor.getValue(), equalTo(1L));
    }

    @Test
    public void testDeleteShouldThrowBadRequestWhenRecordDoesNotExist() throws Exception {
        BookUpdateRequest request = new BookUpdateRequest();
        request.setId(1L);
        request.setTitle("bookTitle");
        request.setIsbn("isbn");
        request.setAuthor("author");
        request.setPublishedDate(LocalDate.now());
        request.setPublisher("publisher");



        when(bookService.findOne(1L)).thenReturn(Optional.ofNullable(null));

        mockMvc.perform(MockMvcRequestBuilders.delete(API_PREFIX+"/1").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.path").value(API_PREFIX + "/1"))
                .andExpect(jsonPath("$.title").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("Book with given ID does not exist"))
                .andExpect(jsonPath("$.status").value(400))
                .andDo(print());
    }

    @Test
    public void testPaginateShouldReturnEmpty() throws Exception {
        int page = 1;
        int pageSize = 10;
        int totalRecords = 0;

        PageImpl<BookResponse> bookResponses = new PageImpl<>(Collections.emptyList(), Pageable.ofSize(pageSize).withPage(page), totalRecords);
        when(bookService.findAll(anyInt(), anyInt())).thenReturn(bookResponses);

        mockMvc.perform(MockMvcRequestBuilders.get(API_PREFIX+ "/paginate").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isEmpty())
                .andExpect(jsonPath("$.pageable.pageNumber").value(page))
                .andExpect(jsonPath("$.pageable.pageSize").value(pageSize))
                .andExpect(jsonPath("$.totalElements").value(totalRecords))
                .andDo(print());
    }

    @Test
    public void testPaginateShouldReturnRecords() throws Exception {
        int page = 1;
        int pageSize = 1;

        BookResponse book1 = new BookResponse();
        book1.setId(1L);
        book1.setTitle("book1");

        BookResponse book2 = new BookResponse();
        book2.setId(2L);
        book2.setTitle("book2");

        PageImpl<BookResponse> bookResponses = new PageImpl<>(List.of(book1, book2), Pageable.ofSize(pageSize).withPage(page - 1), 2);
        when(bookService.findAll(anyInt(), anyInt())).thenReturn(bookResponses);

        mockMvc.perform(MockMvcRequestBuilders.get(API_PREFIX+ "/paginate").contentType(MediaType.APPLICATION_JSON)
                        .param("page", String.valueOf(page)).param("limit", String.valueOf(pageSize)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content[0].id").value(1))
                .andExpect(jsonPath("$.content[0].title").value("book1"))
                .andExpect(jsonPath("$.pageable.pageNumber").value(0))
                .andExpect(jsonPath("$.pageable.pageSize").value(pageSize))
                .andExpect(jsonPath("$.totalElements").value(2))
                .andExpect(jsonPath("$.totalPages").value(2))
                .andDo(print());
    }

}