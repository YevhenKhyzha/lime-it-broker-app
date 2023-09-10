package com.lime.it.lime.it.broker.rest.controller.mapping;

import com.lime.it.lime.it.broker.api.EventMessage;
import com.lime.it.lime.it.broker.service.model.EventSystem;
import com.lime.it.lime.it.broker.service.model.Message;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Named;

import java.util.Optional;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface MessageMapper {

    @Mapping(target = "text", source = "messageText")
    @Mapping(target = "eventSystem", source = "eventSystem", qualifiedByName = "mapEventSystem")
    Message map(EventMessage eventMessage);

    @Named("mapEventSystem")
    default EventSystem mapEventSystem(String eventSystem) {
        return EventSystem.valueOf(Optional.of(eventSystem)
                .filter(value -> !value.isEmpty() && !value.isBlank())
                .orElseThrow(IllegalArgumentException::new)
        );
    }
}
