package com.lime.it.lime.it.broker.boot.configuration;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

@Configuration
@ComponentScan(basePackages = {"com.lime.it.lime.it.broker"})
@EnableAutoConfiguration
@EnableAsync
public class LimeItBrokerApplicationConfiguration {

}
