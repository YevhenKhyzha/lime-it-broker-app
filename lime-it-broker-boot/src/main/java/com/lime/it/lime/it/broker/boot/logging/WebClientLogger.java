package com.lime.it.lime.it.broker.boot.logging;

import lombok.extern.slf4j.Slf4j;
import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.api.Request;
import org.eclipse.jetty.client.api.Response;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.slf4j.MDC;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.reactive.ClientHttpConnector;
import org.springframework.http.client.reactive.ClientHttpRequest;
import org.springframework.http.client.reactive.ClientHttpResponse;
import org.springframework.http.client.reactive.JettyClientHttpConnector;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

@Slf4j
@Component
public class WebClientLogger implements ClientHttpConnector {

    private final ClientHttpConnector clientHttpConnector = new JettyClientHttpConnector(

            new HttpClient(new SslContextFactory.Client()) {

                @Override
                public Request newRequest(URI uri) {
                    return enhance(super.newRequest(uri));
                }
            }
    );

    private Request enhance(Request originalRequest) {
        Map<String, String> mdcMap = MDC.getCopyOfContextMap();
        StringBuilder requestBodyBuilder = new StringBuilder();
        StringBuilder responseBodyBuilder = new StringBuilder();

        originalRequest.onRequestContent(
                (request, content) -> requestBodyBuilder.append(StandardCharsets.UTF_8.decode(content))
        );

        originalRequest.onResponseContent(
                (response, content) -> responseBodyBuilder.append(StandardCharsets.UTF_8.decode(content))
        );

        Consumer<Request> requestConsumer = request -> {
            try {
                Optional.ofNullable(mdcMap).ifPresent(MDC::setContextMap);
                log.info("Request: {} {}\nAccept:{}\n{}",
                        request.getMethod(),
                        request.getURI(),
                        request.getHeaders().get("Accept"),
                        requestBodyBuilder
                );
            } finally {
                MDC.clear();
            }
        };

        originalRequest.onRequestSuccess(requestConsumer::accept);
        originalRequest.onRequestFailure((request, failure) -> {
            requestBodyBuilder.append('\n').append(failure);
            requestConsumer.accept(request);
        });

        Consumer<Response> responseConsumer = response -> {
            try {
                Optional.ofNullable(mdcMap).ifPresent(MDC::setContextMap);
                log.info("Response: status: {}\n{}", response.getStatus(), responseBodyBuilder);
            } finally {
                MDC.clear();
            }
        };

        originalRequest.onResponseSuccess(responseConsumer::accept);
        originalRequest.onResponseFailure((response, failure) -> {
            responseBodyBuilder.append('\n').append(failure);
            responseConsumer.accept(response);
        });

        return originalRequest;
    }

    @Override
    public Mono<ClientHttpResponse> connect(HttpMethod method, URI uri,
            Function<? super ClientHttpRequest, Mono<Void>> requestCallback) {

        return clientHttpConnector.connect(method, uri, requestCallback);
    }
}
