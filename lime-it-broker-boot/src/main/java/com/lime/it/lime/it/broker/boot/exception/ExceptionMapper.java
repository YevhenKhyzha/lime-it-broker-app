package com.lime.it.lime.it.broker.boot.exception;

import com.lime.it.lime.it.broker.api.error.Error;
import com.lime.it.lime.it.broker.api.error.ErrorResponse;
import com.lime.it.lime.it.broker.service.provider.ServiceProviderException;
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
    public ResponseEntity<ErrorResponse> handleException(
            HttpServletRequest httpServletRequest,
            ServiceProviderException serviceProviderException) {

        log.error(serviceProviderException.getMessage());

        return createResponseEntity(
                HttpStatus.BAD_REQUEST,
                httpServletRequest.getRequestURI(),
                "CODE",
                serviceProviderException.getMessage()
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
