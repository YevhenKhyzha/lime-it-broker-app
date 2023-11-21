package com.lime.it.lime.it.broker.web.client.generic.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties("web-client")
public record WebClientConfigurationProperties(
        Retry retry) {
}
