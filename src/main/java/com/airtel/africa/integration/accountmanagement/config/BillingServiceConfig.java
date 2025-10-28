package com.airtel.africa.integration.accountmanagement.config;

import io.smallrye.config.ConfigMapping;

@ConfigMapping(prefix = "external.api.billing")
public interface BillingServiceConfig {

    String partyURL();
    String customerURL();
    String customerAccountURL();
    String auth();

}
