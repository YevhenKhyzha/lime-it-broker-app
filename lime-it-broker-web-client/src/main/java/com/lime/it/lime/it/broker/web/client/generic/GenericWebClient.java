package com.lime.it.lime.it.broker.web.client.generic;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class GenericWebClient {
    public void send(HttpMethod httpMethod, String baseUrl, String body) {
        WebClient webClient = WebClient.builder()
//                .clientConnector() // todo create jetty client connector for logging
                .baseUrl(baseUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, "application/json")
                .build();

        WebClient.ResponseSpec responseSpec = webClient
                .method(httpMethod)
                .bodyValue(body)// todo try with get method and null body value
                .retrieve();
        // todo
//        responseSpec.onStatus(status -> HttpStatus.OK, )

    }
}