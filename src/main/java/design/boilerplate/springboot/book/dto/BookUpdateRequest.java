package design.boilerplate.springboot.book.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;

@Data
@EqualsAndHashCode(callSuper = true)
public class BookUpdateRequest extends BookRequest{

    @NotNull
    private Long id;
}
