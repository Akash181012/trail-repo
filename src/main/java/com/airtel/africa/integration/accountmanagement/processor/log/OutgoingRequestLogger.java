package com.airtel.africa.integration.accountmanagement.processor.log;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@ApplicationScoped
@Named("OutgoingRequestLogger")
public class OutgoingRequestLogger implements Processor {
    private static final Logger LOG = LoggerFactory.getLogger(OutgoingRequestLogger.class);

    @Override
    public void process(Exchange exchange) throws Exception {

        LOG.info("Outgoing API Request URL:{} Headers: {} Body:{}",
                exchange.getIn().getHeader(Exchange.HTTP_URI, String.class),
                exchange.getIn().getHeaders(),
                exchange.getIn().getBody(String.class));
    }
}
