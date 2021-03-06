package com.internet.shop.controller;

import com.internet.shop.dto.error.GeneralErrorDto;
import com.internet.shop.exception.*;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({NotFoundException.class})
    public ResponseEntity<GeneralErrorDto> handleNotFoundException(
            NotFoundException ex) {

        GeneralErrorDto apiError =
                new GeneralErrorDto("Not found", Collections.singletonList(ex.getMessage()));
        return new ResponseEntity<>(
                apiError, new HttpHeaders(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({AlreadyExistsException.class})
    public ResponseEntity<GeneralErrorDto> handleAlreadyExistsException(
            AlreadyExistsException ex) {

        GeneralErrorDto apiError =
                new GeneralErrorDto("Already exists", Collections.singletonList(ex.getMessage()));
        return new ResponseEntity<>(
                apiError, new HttpHeaders(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler({InvalidPasswordException.class})
    public ResponseEntity<GeneralErrorDto> handleInvalidPasswordException(
            InvalidPasswordException ex) {

        GeneralErrorDto apiError =
                new GeneralErrorDto("Invalid password", Collections.singletonList(ex.getMessage()));
        return new ResponseEntity<>(
                apiError, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @Override
    public ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {

        List<String> errors = ex.getBindingResult().getFieldErrors()
                .stream().map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.toList());

        GeneralErrorDto apiError =
                new GeneralErrorDto("Validation failed", errors);
        return new ResponseEntity<>(
                apiError, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

}
