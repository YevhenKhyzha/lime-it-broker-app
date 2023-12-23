package com.lime.it.lime.it.broker.timer;

import com.lime.it.lime.it.broker.timer.exception.TimerNotExistsException;
import com.lime.it.lime.it.broker.timer.exception.TimerSchedulerException;
import com.lime.it.lime.it.broker.timer.job.BrokerTimerJob;
import com.lime.it.lime.it.broker.timer.model.CreateTimerDTO;
import com.lime.it.lime.it.broker.timer.model.TimerInfo;
import com.lime.it.lime.it.broker.timer.model.TimerMessageDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Service;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.Map;
import java.util.Set;

import static com.lime.it.lime.it.broker.timer.utils.JobDataEnum.TIMER_TRACE_MDC_UUID;
import static com.lime.it.lime.it.broker.timer.utils.JobDataEnum.TIMER_MESSAGE;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class TimerService {

    private final SchedulerFactoryBean schedulerFactoryBean;

    public void createTimer(CreateTimerDTO createTimerDTO) {
        log.info("Start create timer with key: name: {} , group: {} , UUId: {}",
                createTimerDTO.timerName(), createTimerDTO.timerGroup(), createTimerDTO.timerUUId());

        JobKey timerJobKey = JobKey
                .jobKey(createTimerDTO.timerName(), createTimerDTO.timerGroup());

        TriggerKey timerTriggerKey = TriggerKey
                .triggerKey(createTimerDTO.timerName(), createTimerDTO.timerGroup());
        try {
            JobDetail jobDetail = JobBuilder.newJob(BrokerTimerJob.class)
                    .withIdentity(timerJobKey)
                    .usingJobData(TIMER_TRACE_MDC_UUID.toString(), MDC.get(TIMER_TRACE_MDC_UUID.toString()))
                    .usingJobData(TIMER_MESSAGE.toString(), createTimerDTO.requestTimerMessageDetails().message())
                    .build();

            Trigger trigger = TriggerBuilder.newTrigger()
                    .forJob(timerJobKey)
                    .withIdentity(timerTriggerKey)
                    .startAt(Date.from(createTimerDTO.timerStartDateTime().toInstant()))
                    .build();

            schedulerFactoryBean.getScheduler().scheduleJob(jobDetail, trigger);

            log.info("Timer with key: name: {} , group: {} , UUId: {} successfully created",
                    createTimerDTO.timerName(), createTimerDTO.timerGroup(), createTimerDTO.timerUUId());
        } catch (SchedulerException e) {
            log.error("Error in creation timer with key: name: {} , group: {} , UUId: {}",
                    createTimerDTO.timerName(), createTimerDTO.timerGroup(), createTimerDTO.timerUUId(),
                    e);

            throw new TimerSchedulerException("Timer creation error", e);
        }
    }

    public void updateTimer(CreateTimerDTO createTimerDTO) {
        log.info("Start update timer with key: name: {} , group: {} , UUId: {}",
                createTimerDTO.timerName(), createTimerDTO.timerGroup(), createTimerDTO.timerUUId());

        JobKey timerJobKey = JobKey
                .jobKey(createTimerDTO.timerName(), createTimerDTO.timerGroup());

        TriggerKey timerTriggerKey = TriggerKey
                .triggerKey(createTimerDTO.timerName(), createTimerDTO.timerGroup());
        try {
            JobDetail jobDetail = JobBuilder.newJob(BrokerTimerJob.class)
                    .withIdentity(timerJobKey)
                    .usingJobData(TIMER_MESSAGE.toString(), createTimerDTO.requestTimerMessageDetails().message())
                    .build();

            Trigger trigger = TriggerBuilder.newTrigger()
                    .forJob(timerJobKey)
                    .withIdentity(timerTriggerKey)
                    .startAt(Date.from(createTimerDTO.timerStartDateTime().toInstant()))
                    .build();

            schedulerFactoryBean.getScheduler().scheduleJob(jobDetail, Set.of(trigger), true);

            log.info("Timer with key: name: {} , group: {} , UUId: {} successfully updated",
                    createTimerDTO.timerName(), createTimerDTO.timerGroup(), createTimerDTO.timerUUId());
        } catch (SchedulerException e) {
            log.error("Error in update timer with key: name: {} , group: {} , UUId: {}",
                    createTimerDTO.timerName(), createTimerDTO.timerGroup(), createTimerDTO.timerUUId(),
                    e);

            throw new TimerSchedulerException("Timer update error", e);
        }
    }

    public TimerInfo getTimer(String timerName, String timerGroup) {
        JobKey timerJobKey = JobKey.jobKey(timerName, timerGroup);
        TriggerKey timerTriggerKey = TriggerKey.triggerKey(timerName, timerGroup);

        try {
            Scheduler scheduler = schedulerFactoryBean.getScheduler();

            if (!scheduler.checkExists(timerJobKey) || !scheduler.checkExists(timerTriggerKey)) {
                throw new TimerNotExistsException(
                        String.format("Timer with name: %s and group: %s not exists", timerName, timerGroup));
            }

            Map<String, Object> jobDetailsMap = scheduler.getJobDetail(timerJobKey).getJobDataMap().getWrappedMap();
            ZonedDateTime timerStartTime = scheduler.getTrigger(timerTriggerKey)
                    .getStartTime().toInstant().atZone(ZoneOffset.UTC);

            return TimerInfo.builder()
                    .timerName(timerName)
                    .timerGroup(timerGroup)
                    .timerTriggeredTime(timerStartTime)
                    .timerMessageDetails(TimerMessageDetails.builder()
                            .message(jobDetailsMap.get(TIMER_MESSAGE.toString()).toString())
                            .build())
                    .build();
        } catch (SchedulerException schedulerException) {
            throw new TimerSchedulerException("Get timer error", schedulerException);
        }
    }

    public TimerInfo deleteTimer(String timerName, String timerGroup) {
        JobKey timerJobKey = JobKey.jobKey(timerName, timerGroup);
        TriggerKey timerTriggerKey = TriggerKey.triggerKey(timerName, timerGroup);

        try {
            Scheduler scheduler = schedulerFactoryBean.getScheduler();

            if (!scheduler.checkExists(timerJobKey) || !scheduler.checkExists(timerTriggerKey)) {
                throw new TimerNotExistsException(
                        String.format("Timer with name: %s and group: %s not exists", timerName, timerGroup));
            }

            scheduler.deleteJob(timerJobKey);

            return TimerInfo.builder()
                    .timerName(timerName)
                    .timerGroup(timerGroup)
                    .build();
        } catch (SchedulerException schedulerException) {
            throw new TimerSchedulerException("Error in deleting timer job", schedulerException);
        }
    }
}