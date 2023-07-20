package ru.stat_server.exception;

import org.springframework.http.HttpStatus;
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
public class ErrorHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    public ErrorResponse handleBadRequestException(final RequestException e, WebRequest webRequest) {
        List<String> errors = new ArrayList<>();
        Arrays.stream(e.getStackTrace()).forEach(error -> errors.add(error.toString()));
        return new ErrorResponse(errors, e.getLocalizedMessage(), "Исключение в" +
                " запросе " + webRequest.getDescription(false), HttpStatus.BAD_REQUEST);
    }
}
