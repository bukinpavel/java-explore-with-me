package ru.practicum.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestControllerAdvice
@ControllerAdvice
@Slf4j
public class ErrorHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFoundException(final NotFoundException e, WebRequest webRequest) {
        List<String> errors = new ArrayList<>();
        Arrays.stream(e.getStackTrace()).forEach(error -> errors.add(error.toString()));
        return new ErrorResponse(errors, e.getLocalizedMessage(),
                "Не найдены переданные данные в запросе " + webRequest.getDescription(false),
                HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleThrowable(final MethodArgumentNotValidException e, WebRequest webRequest) {
        List<String> errors = new ArrayList<>();
        Arrays.stream(e.getStackTrace()).forEach(error -> errors.add(error.toString()));
        log.info("400 {}", e.getMessage(), e);
        return new ErrorResponse(errors, e.getLocalizedMessage(),
                "Не валидные данные " + webRequest.getDescription(false),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorResponse handleEventsException(final EventException e, WebRequest webRequest) {
        List<String> errors = new ArrayList<>();
        Arrays.stream(e.getStackTrace()).forEach(error -> errors.add(error.toString()));
        return new ErrorResponse(errors, e.getLocalizedMessage(), "Исключение в запросе " +
                webRequest.getDescription(false), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleDataIntegrityViolationException(final DataIntegrityViolationException e,
                                                               WebRequest webRequest) {
        List<String> errors = new ArrayList<>();
        Arrays.stream(e.getStackTrace()).forEach(error -> errors.add(error.toString()));
        return new ErrorResponse(errors, e.getLocalizedMessage(), "Исключение в" +
                " запросе " + webRequest.getDescription(false), HttpStatus.CONFLICT);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    public ErrorResponse handleBadRequestException(final RequestException e, WebRequest webRequest) {
        List<String> errors = new ArrayList<>();
        Arrays.stream(e.getStackTrace()).forEach(error -> errors.add(error.toString()));
        return new ErrorResponse(errors, e.getLocalizedMessage(), "Исключение в" +
                " запросе " + webRequest.getDescription(false), HttpStatus.BAD_REQUEST);
    }

}
