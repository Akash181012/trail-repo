package com.airtel.africa.integration.accountmanagement.route;

import jakarta.enterprise.context.ApplicationScoped;


@ApplicationScoped
public class HttpComponentCustomizer {

    /*@Inject
    CamelContext camelContext;

    @Inject
    MyProxyConfigurer myProxyConfigurer;

    @PostConstruct
    void customizeHttpComponent() {
        HttpComponent http = camelContext.getComponent("http", HttpComponent.class);
        http.setHttpClientConfigurer(myProxyConfigurer);
    }*/

    /*@Produces
    @ApplicationScoped
    public ProxyHttpClientConfigurer createProxyHttpClientConfigurer() {
        return new ProxyHttpClientConfigurer("skyhighproxy.ug.airtel.africa", 4145, "http");
    }*/
}
