package design.boilerplate.springboot.book.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

@Data
@EqualsAndHashCode
public class BookResponse {

    private Long id;
    private String isbn;
    private String title;
    private String description;
    private String author;
    private String publisher;
    private LocalDate publishedDate;
}
