package API.EventTom.exceptions;

import API.EventTom.DTO.response.ErrorMessageDTO;

import io.jsonwebtoken.JwtException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;

import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.http.ResponseEntity;

import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.stream.Collectors;

import static API.EventTom.exceptions.ExceptionUtils.buildResponseEntity;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorMessageDTO> handleValidationExceptions(MethodArgumentNotValidException e) {
        String errorMessage = e.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining(", "));

        return buildResponseEntity(errorMessage, HttpStatus.BAD_REQUEST, e);
    }
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ErrorMessageDTO> handleNoHandlerFoundException(NoHandlerFoundException e) {
        return buildResponseEntity("Page does not Exist", HttpStatus.NOT_FOUND, e);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorMessageDTO> handleDataIntegrityViolationException(DataIntegrityViolationException e) {
        return buildResponseEntity("Error handling Data", HttpStatus.INTERNAL_SERVER_ERROR, e);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorMessageDTO> handleMethodNotAllowedException(Exception e) {
        return buildResponseEntity("Method not allowed", HttpStatus.METHOD_NOT_ALLOWED, e);

    }

    @ExceptionHandler(JwtException.class)
    public ResponseEntity<ErrorMessageDTO> handleJwtException(JwtException e) {
        return buildResponseEntity("Invalid token", HttpStatus.UNAUTHORIZED, e);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorMessageDTO> handleRuntimeException(RuntimeException e) {
        return buildResponseEntity("An unexpected error occurred", HttpStatus.INTERNAL_SERVER_ERROR, e);
    }
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorMessageDTO> handleRuntimeException(IllegalArgumentException e) {
        return buildResponseEntity("An unexpected error occurred", HttpStatus.BAD_REQUEST, e);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorMessageDTO> handleException(Exception e) {
        return buildResponseEntity("An unexpected error occurred", HttpStatus.INTERNAL_SERVER_ERROR, e);
    }

}
