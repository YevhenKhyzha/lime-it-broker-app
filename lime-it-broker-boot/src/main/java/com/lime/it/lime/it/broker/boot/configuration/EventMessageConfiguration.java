package com.lime.it.lime.it.broker.boot.configuration;

import com.lime.it.lime.it.broker.service.EventMessageHandlerService;
import com.lime.it.lime.it.broker.service.EventMessageHandlerServiceBroker1;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EventMessageConfiguration {

    @Bean
    @Qualifier("SYSTEM1")
    public EventMessageHandlerService createEventMessageHandlerServiceBroker1() {
        return new EventMessageHandlerServiceBroker1();
    }
}
