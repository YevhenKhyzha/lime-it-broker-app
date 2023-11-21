package com.lime.it.lime.it.broker.web.client.generic;

import com.lime.it.lime.it.broker.web.client.generic.configuration.WebClientConfigurationProperties;
import com.lime.it.lime.it.broker.web.client.generic.exception.WebClientFailedException;
import com.lime.it.lime.it.broker.web.client.generic.exception.WebClientRetryException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ClientHttpConnector;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;
import java.util.Arrays;
import java.util.Optional;

@Component
@EnableConfigurationProperties(WebClientConfigurationProperties.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class GenericWebClient {

    private final ClientHttpConnector clientHttpConnector;
    private final WebClientConfigurationProperties webClientConfigurationProperties;

    public <T> Optional<T> send(HttpMethod httpMethod, String baseUrl, String body, Class<T> responseObjectClass) {
        WebClient webClient = WebClient.builder()
                .clientConnector(clientHttpConnector)
                .baseUrl(baseUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();

        WebClient.ResponseSpec responseSpec = webClient
                .method(httpMethod)
                .bodyValue(body)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .retrieve()          ;

        return responseSpec
                .onStatus(status -> Arrays.asList(webClientConfigurationProperties.retry().statusList())
                                .contains(status.value()),
                        response -> Mono.error(new WebClientRetryException()))
                .bodyToMono(responseObjectClass)
                .onErrorMap(exception -> exception.getCause() != null
                                && Arrays.asList(webClientConfigurationProperties.retry().exceptionList())
                                .contains(exception.getCause().getClass()),
                        exception -> new WebClientRetryException())
                .retryWhen(
                        Retry.backoff(webClientConfigurationProperties.retry().maxRetryAttempts(),
                                        Duration.ofSeconds(webClientConfigurationProperties.retry().minBackoffSeconds()))
                                .jitter(webClientConfigurationProperties.retry().jitterFactor())
                                .filter(throwable -> throwable instanceof WebClientRetryException)
                                .onRetryExhaustedThrow(((retryBackoffSpec, retrySignal) -> {
                                    throw new WebClientFailedException();
                                })))
                .blockOptional();
    }
}