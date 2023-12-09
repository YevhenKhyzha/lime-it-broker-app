package com.lime.it.lime.it.broker.boot.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lime.it.lime.it.broker.service.EventMessageHandlerService;
import com.lime.it.lime.it.broker.service.EventMessageHandlerServiceBroker1;
import com.lime.it.lime.it.broker.web.client.generic.GenericWebClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EventMessageConfiguration {

    @Bean
    @Qualifier("SYSTEM1")
    public EventMessageHandlerService createEventMessageHandlerServiceBroker1(GenericWebClient genericWebClient,
                                                                              ObjectMapper objectMapper) {
        return new EventMessageHandlerServiceBroker1(genericWebClient, objectMapper);
    }
}
