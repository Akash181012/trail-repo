package com.airtel.africa.integration.accountmanagement.route;

import jakarta.enterprise.context.ApplicationScoped;
import org.apache.camel.component.http.HttpClientConfigurer;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.core5.http.HttpHost;
import org.apache.hc.client5.http.config.RequestConfig;

@ApplicationScoped
public class MyProxyConfigurer implements HttpClientConfigurer {

    @Override
    public void configureHttpClient(HttpClientBuilder builder) {
        // Set HTTP proxy for HTTPS requests
        HttpHost proxy = new HttpHost("http", "skyhighproxy.ug.airtel.africa", 4146);

        RequestConfig config = RequestConfig.custom()
                .setProxy(proxy)
                .build();

        builder.setDefaultRequestConfig(config);
    }
}

