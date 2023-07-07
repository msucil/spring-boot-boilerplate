package design.boilerplate.springboot.service.exceptionhandler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
@Slf4j
public class HttpExceptionHandler {

    @ExceptionHandler(HttpClientErrorException.class)
    public ResponseEntity<Map<String, Object>> handleException(HttpClientErrorException ex, HttpServletRequest request){
        final Map<String, Object> response = new HashMap<>(6);
        response.put("title", ex.getStatusCode());
        response.put("status", ex.getRawStatusCode());
        response.put("message", ex.getStatusText());
        response.put("path", request.getPathInfo());

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
