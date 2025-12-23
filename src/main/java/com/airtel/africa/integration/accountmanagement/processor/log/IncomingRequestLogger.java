package com.airtel.africa.integration.accountmanagement.processor.log;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

@ApplicationScoped
@Named("IncomingRequestLogger")
public class IncomingRequestLogger implements Processor {

    private static final Logger LOG = LoggerFactory.getLogger(IncomingRequestLogger.class);

    @Override
    public void process(Exchange exchange) throws Exception {
        MDC.put("traceId", String.valueOf(exchange.getIn().getHeader("X-Request-ID")));
        MDC.put("source", String.valueOf(exchange.getIn().getHeader("X-Channel")));
        LOG.info("Incoming API Request Method:{} URL:{} toD_URL:{} Headers: {} Body:{}",
         exchange.getIn().getHeader(Exchange.HTTP_METHOD, String.class),
         exchange.getIn().getHeader(Exchange.HTTP_URL, String.class),
         exchange.getProperty(Exchange.INTERCEPTED_ENDPOINT, String.class),
         exchange.getIn().getHeaders(),
         exchange.getIn().getBody(String.class));
    }
}
