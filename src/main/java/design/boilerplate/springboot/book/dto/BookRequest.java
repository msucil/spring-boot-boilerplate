package design.boilerplate.springboot.book.dto;

import design.boilerplate.springboot.service.validator.notemptyvalue.NotEmptyValue;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;


@Data
@EqualsAndHashCode
public class BookRequest {

    @NotNull
    @NotEmptyValue
    private String isbn;

    @NotEmptyValue
    @Size(min = 1, max = 50)
    private String title;

    private String description;

    @NotEmptyValue
    private String author;

    @NotEmptyValue
    private String publisher;

    @NotNull
    private LocalDate publishedDate;
}
