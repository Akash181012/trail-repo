package com.airtel.africa.integration.accountmanagement.route;

import com.airtel.africa.integration.accountmanagement.model.party.Error;
import jakarta.enterprise.context.ApplicationScoped;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.bean.validator.BeanValidationException;
import org.apache.camel.model.rest.RestBindingMode;

import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@ApplicationScoped
public abstract class BaseRouteBuilder extends RouteBuilder {

    @Override
    public void configure() throws Exception {

        //HttpClientConfigurer proxyConfigurer = new ProxyHttpClientConfigurer("skyhighproxy.ug.airtel.africa", 4146, "http");

        // Set it on the HTTP component
        //getContext().getComponent("http", org.apache.camel.component.http.HttpComponent.class).setHttpClientConfigurer(proxyConfigurer)

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

