package com.lime.it.lime.it.broker.timer.model;

import lombok.Builder;

@Builder
public record TimerMessageDetails(
        String message) {
}
