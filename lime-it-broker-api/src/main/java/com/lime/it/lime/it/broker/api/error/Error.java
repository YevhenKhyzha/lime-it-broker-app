package com.lime.it.lime.it.broker.api.error;

import lombok.Builder;

@Builder
public record Error(
        String code,
        String detail) {
}
