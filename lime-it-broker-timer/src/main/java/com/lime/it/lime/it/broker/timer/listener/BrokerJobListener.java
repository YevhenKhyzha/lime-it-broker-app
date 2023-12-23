package com.lime.it.lime.it.broker.timer.listener;

import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobListener;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import static com.lime.it.lime.it.broker.timer.utils.JobDataEnum.TIMER_TRACE_MDC_UUID;

@Slf4j
@Component
public class BrokerJobListener implements JobListener {

    @Override
    public String getName() {
        return "BrokerJobListener";
    }

    @Override
    public void jobToBeExecuted(JobExecutionContext jobExecutionContext) {
        if (jobExecutionContext.getMergedJobDataMap().containsKey(TIMER_TRACE_MDC_UUID.toString())) {
            MDC.put(TIMER_TRACE_MDC_UUID.toString(),
                    jobExecutionContext.getMergedJobDataMap().getString(TIMER_TRACE_MDC_UUID.toString()));
        }

        log.info("Timer will be executed");
    }

    @Override
    public void jobExecutionVetoed(JobExecutionContext jobExecutionContext) {
        log.info("vetoed");
    }

    @Override
    public void jobWasExecuted(JobExecutionContext jobExecutionContext, JobExecutionException e) {
        if (jobExecutionContext.getMergedJobDataMap().containsKey(TIMER_TRACE_MDC_UUID.toString())) {
            MDC.remove(TIMER_TRACE_MDC_UUID.toString());
        }

        log.info("Timer was executed");
    }
}
