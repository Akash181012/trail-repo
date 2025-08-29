/*
package com.airtel.africa.integration.accountmanagement.processor;



import com.airtel.africa.integration.accountmanagement.model.party.Individual;
import com.airtel.africa.integration.accountmanagement.service.RedisCacheService;
import io.vertx.redis.client.Command;
import io.vertx.redis.client.Redis;
import io.vertx.redis.client.Request;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.vertx.mutiny.redis.client.Response;

import java.time.Duration;
import java.util.Map;

@ApplicationScoped
@Named("CreatePartyProcessor")
public class CreatePartyProcessor implements Processor {

    private static final Logger log = LoggerFactory.getLogger(CreatePartyProcessor.class);
    @Inject
    Redis redis;

    @Inject
    RedisCacheService redisCacheService;

    @Override
    public void process(Exchange exchange) throws Exception {
        log.info("Body:" + exchange.getMessage().getBody().toString());
        Individual party = exchange.getMessage().getBody(Individual.class);
        String key = "party:" + party.getId();
        String value = exchange.getMessage().getBody(String.class);
        redisCacheService.putValue(key, party);
        Thread.sleep(5000);
        String response = redisCacheService.getValue(key, String.class);
        log.info("redis response: " + response);

        */
/*log.info(key + value);
        redis.send(Request.cmd(Command.SET).arg(key).arg(value).arg(value));
        Thread.sleep(5000);
        redis.send(Request.cmd(Command.SET).arg(key).arg(value)).onSuccess(response -> {
            log.info("Set command successful!");
        }).onFailure(error -> {
            System.err.println("Set command failed: " + error.getMessage());
        });
        Thread.sleep(5000);
        Response redisResponse = redis.send(Request.cmd(Command.GET).arg(key)).isComplete();*//*

    }
}
*/
