package info.nahid.exception;

import info.nahid.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.persistence.EntityNotFoundException;

@ControllerAdvice
@RestController
public class ExceptionErrorHandler {

    @ExceptionHandler(ConstraintsViolationException.class)
    @ResponseBody
    ResponseEntity<ApiResponse> onConstraintValidationException(ConstraintsViolationException exception) {
        return ResponseEntity.badRequest().body(new ApiResponse(false, exception.getMessage()));
    }

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    ResponseEntity<ApiResponse> entityNotFoundException(EntityNotFoundException exception) {
        return new ResponseEntity<>(new ApiResponse(false, exception.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ResponseEntity<ApiResponse> illegalArgumentException(IllegalArgumentException exception) {
        return new ResponseEntity<>(new ApiResponse(false, exception.getMessage()), HttpStatus.BAD_REQUEST);
    }
}
