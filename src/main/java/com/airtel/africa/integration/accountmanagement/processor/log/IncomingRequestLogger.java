package com.airtel.africa.integration.accountmanagement.processor.log;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationScoped
@Named("IncomingRequestLogger")
public class IncomingRequestLogger implements Processor {

    private static final Logger LOG = LoggerFactory.getLogger(IncomingRequestLogger.class);

    @Override
    public void process(Exchange exchange) throws Exception {
        LOG.info("=== Incoming Request ===");
        LOG.info("URL     : {}", exchange.getFromEndpoint().getEndpointUri());
        LOG.info("Method  : {}", exchange.getIn().getHeader(Exchange.HTTP_METHOD, String.class));
        LOG.info("Path    : {}", exchange.getIn().getHeader(Exchange.HTTP_URI, String.class));
        LOG.info("Query   : {}", exchange.getIn().getHeader(Exchange.HTTP_QUERY, String.class));
        LOG.info("Headers : {}", exchange.getIn().getHeaders());
        LOG.info("Body    : {}", exchange.getIn().getBody(String.class));
    }
}
