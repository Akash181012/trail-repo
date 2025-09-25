package com.airtel.africa.integration.accountmanagement.route;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.apache.camel.CamelContext;
import org.apache.camel.component.http.HttpComponent;


@ApplicationScoped
public class HttpComponentCustomizer {

    @Inject
    CamelContext camelContext;

    @Inject
    MyProxyConfigurer myProxyConfigurer;

    @PostConstruct
    void customizeHttpComponent() {
        HttpComponent http = camelContext.getComponent("http", HttpComponent.class);
        http.setHttpClientConfigurer(myProxyConfigurer);
    }
}
