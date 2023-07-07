package design.boilerplate.springboot.service.exceptionhandler;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class FieldError {

    private String field;
    private Object value;
    private String message;
}
