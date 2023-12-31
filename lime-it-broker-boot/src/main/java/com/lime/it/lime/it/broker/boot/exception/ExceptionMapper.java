package com.lime.it.lime.it.broker.boot.exception;

import com.lime.it.lime.it.broker.api.error.Error;
import com.lime.it.lime.it.broker.api.error.ErrorResponse;
import com.lime.it.lime.it.broker.service.provider.ServiceProviderException;
import com.lime.it.lime.it.broker.timer.exception.TimerNotExistsException;
import com.lime.it.lime.it.broker.timer.exception.TimerSchedulerException;
import com.lime.it.lime.it.broker.web.client.generic.exception.WebClientFailedException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

@Slf4j
@RestControllerAdvice
public class ExceptionMapper {

    @ExceptionHandler(ServiceProviderException.class)
    public ResponseEntity<ErrorResponse> handleServiceProviderException(
            HttpServletRequest httpServletRequest,
            ServiceProviderException serviceProviderException) {

        log.error(serviceProviderException.getMessage());

        return createResponseEntity(
                HttpStatus.BAD_REQUEST,
                httpServletRequest.getRequestURI(),
                "CODE1",
                serviceProviderException.getMessage()
        );
    }

    @ExceptionHandler(WebClientFailedException.class)
    public ResponseEntity<ErrorResponse> handleWebClientFailedException(
            HttpServletRequest httpServletRequest,
            WebClientFailedException webClientFailedException) {

        log.error(webClientFailedException.getMessage());

        return createResponseEntity(
                HttpStatus.INTERNAL_SERVER_ERROR,
                httpServletRequest.getRequestURI(),
                "CODE2",
                webClientFailedException.getMessage()
        );
    }

   @ExceptionHandler(TimerSchedulerException.class)
    public ResponseEntity<ErrorResponse> handleTimerSchedulerException(
            HttpServletRequest httpServletRequest,
            TimerSchedulerException timerSchedulerException) {

        log.error(timerSchedulerException.getMessage());

        return createResponseEntity(
                HttpStatus.INTERNAL_SERVER_ERROR,
                httpServletRequest.getRequestURI(),
                "CODE3",
                timerSchedulerException.getMessage()
        );
    }

    @ExceptionHandler(TimerNotExistsException.class)
    public ResponseEntity<ErrorResponse> handleTimerNotExistsException(
            HttpServletRequest httpServletRequest,
            TimerNotExistsException timerNotExistsException) {

        log.error(timerNotExistsException.getMessage());

        return createResponseEntity(
                HttpStatus.NOT_FOUND,
                httpServletRequest.getRequestURI(),
                "CODE4",
                timerNotExistsException.getMessage()
        );
    }

    private ResponseEntity<ErrorResponse> createResponseEntity(
            HttpStatus httpStatus,
            String requestUri,
            String code,
            String detail) {

        return ResponseEntity
                .status(httpStatus)
                .body(
                        ErrorResponse.builder()
                                .timestamp(ZonedDateTime.now(ZoneId.systemDefault()))
                                .path(requestUri)
                                .errors(List.of(
                                        Error.builder()
                                                .code(code)
                                                .detail(detail)
                                                .build()
                                ))
                                .build()
                );
    }
}
