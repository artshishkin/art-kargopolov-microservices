package net.shyshkin.study.app.ws.exceptions;

import lombok.extern.slf4j.Slf4j;
import net.shyshkin.study.app.ws.ui.model.ErrorMessage;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Arrays;
import java.util.Date;

@Slf4j
@RestControllerAdvice
public class AppExceptionHandler extends ResponseEntityExceptionHandler {

//    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorMessage handleAnyException(Exception ex, WebRequest webRequest) {
//        log.error("Handling Exception", ex);
        String message = ex.getLocalizedMessage();
        if (message == null) message = ex.toString();
        return ErrorMessage.builder()
                .message(message)
                .timestamp(new Date())
                .build();
    }

    @ExceptionHandler(NullPointerException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorMessage handleNPE(NullPointerException ex, WebRequest webRequest) {
        String message = Arrays.toString(ex.getStackTrace());
        return ErrorMessage.builder()
                .message(message)
                .timestamp(new Date())
                .build();
    }
}
