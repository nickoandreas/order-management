package nandreas.ordermanagement.controller;

import jakarta.validation.ConstraintViolationException;
import nandreas.ordermanagement.dto.ResponseStatus;
import nandreas.ordermanagement.dto.WebResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

@RestControllerAdvice
public class ErrorController
{
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<WebResponse<String>> constraintViolationException(ConstraintViolationException exception)
    {
        return ResponseEntity.badRequest().body(WebResponse.<String>builder()
                .status(ResponseStatus.FAILED.toLowerCase())
                .message(exception.getMessage())
                .build());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<WebResponse<String>> illegalArgumentException(IllegalArgumentException exception)
    {
        return ResponseEntity.badRequest().body(WebResponse.<String>builder()
                .status(ResponseStatus.FAILED.toLowerCase())
                .message(exception.getMessage())
                .build());
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<WebResponse<String>> apiException(ResponseStatusException exception)
    {
        return ResponseEntity.status(exception.getStatusCode()).body(WebResponse.<String>builder()
                .status(ResponseStatus.FAILED.toLowerCase())
                .message(exception.getReason())
                .build());
    }
}
