package com.lime.it.lime.it.broker.service;

import com.lime.it.lime.it.broker.service.model.Message;
import com.lime.it.lime.it.broker.web.client.testmodel.TestModelWebClient;
import com.lime.it.test.web.api.TestModel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Qualifier("SYSTEM2")
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class EventMessageHandlerServiceBroker2 implements EventMessageHandlerService {

    private final TestModelWebClient testModelWebClient;

    @Override
    public void handle(Message message) {
        log.info("broker2");
        log.info(message.text());

        TestModel testModelToSend = new TestModel(message.eventType(), message.text());

        Optional<TestModel> testModelOptional = testModelWebClient.postTestModel(testModelToSend);
        testModelOptional.ifPresent(testModelResponse ->
                log.info("Response: " + testModelResponse.value1() + ", " + testModelResponse.value2()));
    }
}
