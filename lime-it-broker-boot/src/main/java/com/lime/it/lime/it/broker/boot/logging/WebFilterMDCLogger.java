package com.lime.it.lime.it.broker.boot.logging;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;
import java.util.UUID;

import static com.lime.it.lime.it.broker.timer.utils.JobDataEnum.TIMER_TRACE_MDC_UUID;

@Slf4j
@Component
@WebFilter(urlPatterns = {"/api/timer/*"})
public class WebFilterMDCLogger implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        // it is needed to trace flow after timer will be fired
        MDC.put(TIMER_TRACE_MDC_UUID.toString(), UUID.randomUUID().toString());

        try {
            chain.doFilter(request, response);
        } finally {
            MDC.clear();
        }
    }
}
