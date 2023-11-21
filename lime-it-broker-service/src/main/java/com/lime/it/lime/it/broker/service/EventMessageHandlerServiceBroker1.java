package com.lime.it.lime.it.broker.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lime.it.lime.it.broker.service.model.Message;
import com.lime.it.lime.it.broker.web.client.generic.GenericWebClient;
import com.lime.it.test.web.api.TestModel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.scheduling.annotation.Async;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
public class EventMessageHandlerServiceBroker1 implements EventMessageHandlerService {

    private final GenericWebClient genericWebClient;
    private final ObjectMapper objectMapper;

    @Async
    @Override
    public void handle(Message message) {
        log.info("broker1");
        log.info(message.text());

        String body;
        try {
            TestModel testModel = new TestModel(message.eventType(), message.text());
            body = objectMapper.writeValueAsString(testModel);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e.getMessage());
        }

        Optional<TestModel> testModelOptional = genericWebClient
                .send(HttpMethod.POST, "http://localhost:8090/api/test", body, TestModel.class);

        testModelOptional.ifPresent(testModel -> {
            log.info(testModel.value1());
            log.info(testModel.value2());
        });
    }
}
