package com.lime.it.lime.it.broker.service;

import com.lime.it.lime.it.broker.service.model.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Slf4j
@Qualifier("SYSTEM2")
@Service
public class EventMessageHandlerServiceBroker2 implements EventMessageHandlerService {

    @Override
    public void handle(Message message) {
        log.info("broker2");
        log.info(message.text());
    }
}
