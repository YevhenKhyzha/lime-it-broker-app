package com.lime.it.lime.it.broker.service.model;

import lombok.Builder;

@Builder
public record Message(
        String eventType,
        EventSystem eventSystem,
        String text) {
}
