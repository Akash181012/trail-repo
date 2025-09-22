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
        LOG.infof("External API Call URL: %s", exchange.getProperty(Exchange.TO_ENDPOINT, String.class));
        LOG.infof("External API Call Request: %s", exchange.getMessage().getBody(String.class));
        LOG.infof("External API Call Headers: %s", exchange.getMessage().getHeaders());
        LOG.infof("External API Call Paramns: %s", exchange.getIn().getHeader(Exchange.HTTP_QUERY, String.class));
    }
}
