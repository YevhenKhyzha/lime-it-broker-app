package com.lime.it.lime.it.broker.rest.controller;

import com.lime.it.lime.it.broker.api.timer.CreateTimerRequest;
import com.lime.it.lime.it.broker.api.timer.TimerInfoResponse;
import com.lime.it.lime.it.broker.rest.controller.mapping.TimerMapper;
import com.lime.it.lime.it.broker.timer.TimerService;
import com.lime.it.lime.it.broker.timer.model.CreateTimerDTO;
import com.lime.it.lime.it.broker.timer.model.TimerInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/api", produces = "application/json;charset-UTF-8")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class TimerController {

    private final TimerService timerService;
    private final TimerMapper timerMapper;

    @PostMapping("/timer")
    @ResponseBody
    public ResponseEntity<Void> createTimer(@Valid @RequestBody CreateTimerRequest createTimerRequest) {
        CreateTimerDTO createTimerDTO = timerMapper.map(createTimerRequest);

        timerService.createTimer(createTimerDTO);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/timer")
    @ResponseBody
    public ResponseEntity<Void> updateTimer(@Valid @RequestBody CreateTimerRequest createTimerRequest) {
        CreateTimerDTO createTimerDTO = timerMapper.map(createTimerRequest);

        timerService.updateTimer(createTimerDTO);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/timer/{name}/{group}")
    @ResponseBody
    public ResponseEntity<TimerInfoResponse> getTimer(@PathVariable("name") String timerName,
                                                      @PathVariable("group") String timerGroup) {
        TimerInfo timerInfo = timerService.getTimer(timerName, timerGroup);

        TimerInfoResponse timerInfoResponse = timerMapper.map(timerInfo);

        return ResponseEntity.ok(timerInfoResponse);
    }

    @DeleteMapping("/timer/{name}/{group}")
    @ResponseBody
    public ResponseEntity<TimerInfoResponse> deleteTimer(@PathVariable("name") String timerName,
                                                         @PathVariable("group") String timerGroup) {
        TimerInfo timerInfo = timerService.deleteTimer(timerName, timerGroup);

        TimerInfoResponse timerInfoResponse = timerMapper.map(timerInfo);

        return ResponseEntity.ok(timerInfoResponse);
    }
}
