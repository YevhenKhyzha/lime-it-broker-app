package com.lime.it.lime.it.broker.timer.job;

import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import static com.lime.it.lime.it.broker.timer.utils.JobDataEnum.TIMER_MESSAGE;

@Slf4j
public class BrokerTimerJob implements Job {

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        try {
            JobDataMap jobDataMap = jobExecutionContext.getMergedJobDataMap();

            log.info(jobDataMap.getString(TIMER_MESSAGE.toString()));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }
}
