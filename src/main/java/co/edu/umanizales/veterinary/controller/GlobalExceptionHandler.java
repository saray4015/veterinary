package co.edu.umanizales.veterinary.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({IllegalArgumentException.class, IllegalStateException.class})
    public ResponseEntity<String> handleIllegal(RuntimeException e) {
        String msg = e.getMessage();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(msg != null ? msg : "Bad request");
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<String> handleNotReadable(HttpMessageNotReadableException e) {
        String msg = e.getMostSpecificCause() != null ? e.getMostSpecificCause().getMessage() : e.getMessage();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid request body: " + (msg != null ? msg : "Unreadable JSON"));
    }
}
