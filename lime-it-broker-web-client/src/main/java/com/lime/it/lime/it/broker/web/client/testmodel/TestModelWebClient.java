package com.lime.it.lime.it.broker.web.client.testmodel;

import com.lime.it.lime.it.broker.web.client.generic.configuration.WebClientConfigurationProperties;
import com.lime.it.lime.it.broker.web.client.generic.exception.WebClientFailedException;
import com.lime.it.lime.it.broker.web.client.generic.exception.WebClientRetryException;
import com.lime.it.test.web.api.TestModel;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;
import java.util.Arrays;
import java.util.Optional;

@Service
@EnableConfigurationProperties(WebClientConfigurationProperties.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class TestModelWebClient {

    // another way of creation web client. define it in another bean with customizer class adding logger
    private final WebClient testApplicationModelWebClient;
    private final WebClientConfigurationProperties webClientConfigurationProperties;

    public Optional<TestModel> postTestModel(TestModel testModel) {
        WebClient.ResponseSpec responseSpec =  testApplicationModelWebClient
                .post()
                .uri("/api/test")
                .bodyValue(testModel)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .retrieve();

        return responseSpec
                .onStatus(status -> Arrays.asList(webClientConfigurationProperties.retry().statusList())
                                .contains(status.value()),
                        response -> Mono.error(new WebClientRetryException()))
                .bodyToMono(TestModel.class)
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
