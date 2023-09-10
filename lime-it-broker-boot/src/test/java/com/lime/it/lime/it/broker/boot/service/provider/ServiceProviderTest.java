package com.lime.it.lime.it.broker.boot.service.provider;

import com.lime.it.lime.it.broker.service.EventMessageHandlerService;
import com.lime.it.lime.it.broker.service.model.Message;
import com.lime.it.lime.it.broker.service.provider.ServiceProvider;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(
        webEnvironment = RANDOM_PORT,
        // if config in application.yml is missing it is needed to disable it
        properties = {"spring.cloud.config.enabled=false"}
)
class ServiceProviderTest {

    @Autowired
    private ServiceProvider<EventMessageHandlerService> eventMessageHandlerServiceServiceProvider;

    @Test
    void testServiceProvider() {
        EventMessageHandlerService serviceProvided = eventMessageHandlerServiceServiceProvider
                .provide(EventMessageHandlerService.class, "SYSTEM1");
        serviceProvided.handle(Message.builder().build());

        EventMessageHandlerService serviceProvided2 = eventMessageHandlerServiceServiceProvider
                .provide(EventMessageHandlerService.class, "SYSTEM2");
        serviceProvided2.handle(Message.builder().build());
    }
}