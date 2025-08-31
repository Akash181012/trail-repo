package com.airtel.africa.integration.accountmanagement.route;

import com.airtel.africa.integration.accountmanagement.model.party.Error;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.bean.validator.BeanValidationException;
import org.apache.camel.model.rest.RestBindingMode;
import org.apache.camel.support.jsse.KeyStoreParameters;
import org.apache.camel.support.jsse.SSLContextParameters;
import org.apache.camel.support.jsse.TrustManagersParameters;

import java.beans.JavaBean;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@ApplicationScoped
public abstract class BaseRouteBuilder extends RouteBuilder {

    @Produces
    public SSLContextParameters externalApiSsl() {
        KeyStoreParameters ksp = new KeyStoreParameters();
        ksp.setResource("/etc/certs/truststore.p12");
        ksp.setPassword("changeit");

        TrustManagersParameters tmp = new TrustManagersParameters();
        tmp.setKeyStore(ksp);

        SSLContextParameters ssl = new SSLContextParameters();
        ssl.setTrustManagers(tmp);
        return ssl;
    }

    @Override
    public void configure() throws Exception {

        restConfiguration()
                .component("platform-http")
                .contextPath("/api")
                .bindingMode(RestBindingMode.json)
                .clientRequestValidation(true);

        onException(BeanValidationException.class).handled(true)
                .process(exchange -> {
                    Exception ex = exchange.getProperty(Exchange.EXCEPTION_CAUGHT, Exception.class);
                    String message=ex.getMessage();
                    log.error(message);
                    Pattern pattern = Pattern.compile("errors:\\s*\\[(.*?)\\]");
                    Matcher matcher = pattern.matcher(ex.getMessage());
                    if (matcher.find()) {
                         message = matcher.group(1);
                    }
                    Error error = new Error();
                    error.setCode("400");
                    error.setReason("Validation Error");
                    error.setMessage(message);
                    error.setStatus("400");
                    error.setReferenceError(UUID.randomUUID().toString());
                    //error.setAtType("Error");
                    //error.setAtBaseType("Error");
                    //error.setAtSchemaLocation("https://my-schema-location");

                    exchange.getMessage().setBody(error);
                    exchange.getMessage().setHeader(Exchange.CONTENT_TYPE, "application/json");
                    exchange.getMessage().setHeader(Exchange.HTTP_RESPONSE_CODE, 400);
                }).marshal().json();


        onException(org.apache.camel.ValidationException.class).handled(true)
                    .process(exchange -> {
                        Exception ex = exchange.getProperty(Exchange.EXCEPTION_CAUGHT, Exception.class);
                        String message=ex.getMessage();
                        Error error = new Error();
                        error.setCode("400");
                        error.setReason("Validation Error");
                        error.setMessage(message);
                        error.setStatus("400");
                        exchange.getMessage().setBody(error);
                        exchange.getMessage().setHeader(Exchange.CONTENT_TYPE, "application/json");
                        exchange.getMessage().setHeader(Exchange.HTTP_RESPONSE_CODE, 400);
                    }).marshal().json();

        onException(Exception.class).handled(true).process(exchange -> {
            Exception ex = exchange.getProperty(Exchange.EXCEPTION_CAUGHT, Exception.class);
            log.error(ex.getMessage());
            String message=ex.getMessage();
            log.error(message);
            Error error = new Error();
            error.setCode("500");
            error.setReason(" Error");
            error.setMessage("Server Error");
            error.setStatus("roo");

            exchange.getMessage().setBody(error);
            exchange.getMessage().setHeader(Exchange.CONTENT_TYPE, "application/json");
            exchange.getMessage().setHeader(Exchange.HTTP_RESPONSE_CODE, 400);
        }).marshal().json();

    }
}

