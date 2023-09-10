package com.lime.it.lime.it.broker.service;

import com.lime.it.lime.it.broker.service.model.Message;

public interface EventMessageHandlerService {

    void handle(Message message);
}