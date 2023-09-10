package com.lime.it.lime.it.broker.rest.controller;

import com.lime.it.lime.it.broker.api.EventMessage;
import com.lime.it.lime.it.broker.rest.controller.mapping.MessageMapper;
import com.lime.it.lime.it.broker.service.EventMessageHandlerService;
import com.lime.it.lime.it.broker.service.model.Message;
import com.lime.it.lime.it.broker.service.provider.ServiceProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/message")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class MessageController {

    private final ServiceProvider<EventMessageHandlerService> eventMessageHandlerServiceProvider;
    private final MessageMapper messageMapper;

    @PostMapping("/consume")
    @ResponseBody
    public ResponseEntity<EventMessage> consumeMessage(@Valid @RequestBody EventMessage eventMessage) {
        Message message = messageMapper.map(eventMessage);

        eventMessageHandlerServiceProvider.provide(EventMessageHandlerService.class, message.eventSystem().toString())
                .handle(message);

        return ResponseEntity.ok().body(eventMessage);
    }
}