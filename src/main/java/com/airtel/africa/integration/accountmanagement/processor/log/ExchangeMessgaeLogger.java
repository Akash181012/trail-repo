package com.airtel.africa.integration.accountmanagement.processor.log;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.time.Duration;
import java.time.Instant;

@ApplicationScoped
@Named("exchangeMessgaeLogger")
public class ExchangeMessgaeLogger implements Processor {

    private static final Logger log = LoggerFactory.getLogger(ExchangeMessgaeLogger.class);

   /* @Inject
    LoggingConfig loggingConfig;*/

    @Override
    public void process(Exchange exchange) {
        String startTimeStr = exchange.getProperty("startTime", String.class);
        Long durationMs =null;
        if(startTimeStr==null) {
            exchange.setProperty("startTime", Instant.now().toString());
            MDC.put("traceId", String.valueOf(exchange.getIn().getHeader("X-Request-ID")));
            MDC.put("source", String.valueOf(exchange.getIn().getHeader("X-Channel")));
        } else {
            Instant startTime = Instant.parse(startTimeStr);
            durationMs = Duration.between(startTime, Instant.now()).toMillis();
        }

        String direction = exchange.getProperty("direction", String.class);
        String body = exchange.getIn().getBody(String.class);

        log.info("MessageSystem ExchangeId:{}, Direction:{}, Endpoint:{}, Headers:{}, Body:{} Duration: {}",
                exchange.getExchangeId(), direction,
                exchange.getProperty(Exchange.TO_ENDPOINT, String.class)==null?
                        exchange.getFromEndpoint().getEndpointUri():
                        exchange.getProperty(Exchange.TO_ENDPOINT, String.class),
                exchange.getIn().getHeaders(), body, durationMs);
    }

    /*private Map<String, Object> maskHeaders(Map<String, Object> headers) {
        if (headers == null) return Collections.emptyMap();
        Set<String> sensitiveHeaders = loggingConfig.headers().stream()
                .collect(Collectors.toSet());
        Map<String, Object> maskedHeaders = null;
        if(headers!=null) {
            maskedHeaders = headers.entrySet().stream()
                    .filter(e -> e.getKey() != null && e.getValue() != null)
                    .collect(Collectors.toMap(
                            Map.Entry::getKey,
                            e -> sensitiveHeaders.contains(e.getKey()) ? "****" : e.getValue()
                    ));
        }
        return maskedHeaders;
    }*/
}

