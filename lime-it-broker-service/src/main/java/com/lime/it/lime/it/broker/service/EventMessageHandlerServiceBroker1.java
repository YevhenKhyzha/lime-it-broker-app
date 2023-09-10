package com.lime.it.lime.it.broker.service;

import com.lime.it.lime.it.broker.service.model.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;

@Slf4j
public class EventMessageHandlerServiceBroker1 implements EventMessageHandlerService {

    @Async
    @Override
    public void handle(Message message) {
        log.info("broker1");
        log.info(message.text());
    }
}
