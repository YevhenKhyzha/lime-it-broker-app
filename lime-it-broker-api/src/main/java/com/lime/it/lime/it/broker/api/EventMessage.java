package com.lime.it.lime.it.broker.api;

import lombok.Builder;

import javax.validation.constraints.NotBlank;

@Builder
public record EventMessage(
        @NotBlank String eventType,
        @NotBlank String eventSystem,
        @NotBlank String messageText) {
}