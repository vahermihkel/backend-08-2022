package ee.mihkel.webshop.controller.exceptions;

import ee.mihkel.webshop.controller.model.ExceptionResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Date;
import java.util.NoSuchElementException;

@ControllerAdvice   // ta hakkab nüüd KÕIKIDE controllerite Exceptioneid kinni püüdma
public class ControllerExceptionHandler {

    @ExceptionHandler()
    public ResponseEntity<ExceptionResponse> handleError(NoSuchElementException e) {
        ExceptionResponse response = getExceptionResponse(HttpStatus.NOT_FOUND, "Element not found");
        return new ResponseEntity<>(response,HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler()
    public ResponseEntity<ExceptionResponse> handleError(HttpMessageNotReadableException e) {
        ExceptionResponse response = getExceptionResponse(HttpStatus.BAD_REQUEST, "Body is missing");
        return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler()
    public ResponseEntity<ExceptionResponse> handleError(HttpRequestMethodNotSupportedException e) {
        ExceptionResponse response = getExceptionResponse(HttpStatus.BAD_REQUEST, "Wrong method type");
        return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler()
    public ResponseEntity<ExceptionResponse> handleError(CategoryInUseException e) {
        ExceptionResponse response = getExceptionResponse(HttpStatus.BAD_REQUEST, "CATEGORY_IS_IN_USE");
        return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
    }

    private ExceptionResponse getExceptionResponse(HttpStatus notFound, String s) {
        // ctrl + alt + m
        ExceptionResponse response = new ExceptionResponse();
        response.setDate(new Date());
        response.setError(notFound);
        response.setStatusCode(notFound.value());
        response.setMessage(s);
        return response;
    }
}
