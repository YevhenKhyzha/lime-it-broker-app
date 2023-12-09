package com.lime.it.lime.it.broker.boot.webclient;

import com.lime.it.lime.it.broker.boot.logging.WebClientLogger;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.reactive.function.client.WebClientCustomizer;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class WebClientBuilderCommonCustomizer implements WebClientCustomizer {

    private final WebClientLogger webClientLogger;

    @Override
    public void customize(WebClient.Builder webClientBuilder) {
        webClientBuilder.clientConnector(webClientLogger);
    }
}
