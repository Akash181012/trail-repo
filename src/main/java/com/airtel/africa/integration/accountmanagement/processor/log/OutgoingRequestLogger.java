package com.airtel.africa.integration.accountmanagement.processor.log;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.jboss.logging.Logger;

@ApplicationScoped
@Named("OutgoingRequestLogger")
public class OutgoingRequestLogger implements Processor {
    private static final Logger LOG = Logger.getLogger(OutgoingRequestLogger.class);

    @Override
    public void process(Exchange exchange) throws Exception {
        LOG.info("=== Outgoing Request ===");
        LOG.infof("Response Body: %s", exchange.getMessage().getBody(String.class));
        LOG.infof("Response Headers: %s", exchange.getMessage().getHeaders());
    }
}
