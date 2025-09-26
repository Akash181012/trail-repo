package com.airtel.africa.integration.accountmanagement.route;

import io.quarkus.runtime.Startup;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import org.apache.camel.component.http.HttpClientConfigurer;
import org.apache.camel.component.http.HttpComponent;
import org.apache.camel.component.http.ProxyHttpClientConfigurer;

/*@ApplicationScoped
public class HttpClientConfig {

    @Produces
    @Startup
    public HttpComponent configureHttpComponent() {
        try {
            HttpComponent httpComponent = new HttpComponent();
            HttpClientConfigurer proxyConfigurer = new ProxyHttpClientConfigurer("skyhighproxy.ug.airtel.africa", 4146, "http");
            httpComponent.setHttpClientConfigurer(proxyConfigurer);
            return httpComponent;
        } catch (Exception e) {
            throw new RuntimeException("Failed to configure HTTP component", e);
        }
    }
}*/
