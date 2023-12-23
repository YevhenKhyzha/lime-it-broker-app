package com.lime.it.lime.it.broker.rest.controller.mapping;

import com.lime.it.lime.it.broker.api.timer.CreateTimerRequest;
import com.lime.it.lime.it.broker.api.timer.TimerInfoResponse;
import com.lime.it.lime.it.broker.timer.model.CreateTimerDTO;
import com.lime.it.lime.it.broker.timer.model.TimerInfo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface TimerMapper {

    @Mapping(target = "requestTimerMessageDetails.message", source = "requestTimerMessageDetails.message")
    CreateTimerDTO map(CreateTimerRequest createTimerRequest);

    @Mapping(target = "timerInfoMessageDetails.message", source = "timerMessageDetails.message")
    TimerInfoResponse map(TimerInfo timerInfo);
}
