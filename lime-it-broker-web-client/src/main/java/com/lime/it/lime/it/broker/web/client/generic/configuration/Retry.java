package com.lime.it.lime.it.broker.web.client.generic.configuration;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public record Retry(
        @NotNull Integer maxRetryAttempts,
        @NotNull Integer minBackoffSeconds,
        @NotNull Double jitterFactor,
        @NotEmpty Integer[] statusList,
        @NotEmpty Class<? extends Throwable>[] exceptionList) {
}
