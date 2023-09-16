package com.lime.it.lime.it.broker.api.error;

import lombok.Builder;

import java.time.ZonedDateTime;
import java.util.List;

@Builder
public record ErrorResponse(
        ZonedDateTime timestamp,
        String path,
        String detail,
        List<Error> errors) {
}
