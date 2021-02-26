package net.shyshkin.study.photoapp.api.gateway;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Slf4j
@Configuration
//They have LOWEST priority (order not set but between 2147483646 and 2147483647)
public class GlobalFilterConfiguration {

    @Order(1)
    @Bean
    public GlobalFilter mySecondGlobalFilter() {
        return (exchange, chain) -> {
            log.debug("My second Pre-Filter is executed with path {} and headers: {}", exchange.getRequest().getPath(), exchange.getRequest().getHeaders());
            return chain
                    .filter(exchange)
                    .then(Mono.fromRunnable(() -> log.debug("My second Post-Filter is executed with headers: {}", exchange.getResponse().getHeaders())));
        };
    }

    @Order(3)
    @Bean
    public GlobalFilter myThirdGlobalFilter() {
        return (exchange, chain) -> {
            log.debug("My third Pre-Filter is executed with path {} and headers: {}", exchange.getRequest().getPath(), exchange.getRequest().getHeaders());
            return chain
                    .filter(exchange)
                    .then(Mono.fromRunnable(() -> log.debug("My third Post-Filter is executed with headers: {}", exchange.getResponse().getHeaders())));
        };
    }

    @Order(2)
    @Bean
    public GlobalFilter myFourthGlobalFilter() {
        return (exchange, chain) -> {
            log.debug("My fourth Pre-Filter is executed with path {} and headers: {}", exchange.getRequest().getPath(), exchange.getRequest().getHeaders());
            return chain
                    .filter(exchange)
                    .then(Mono.fromRunnable(() -> log.debug("My fourth Post-Filter is executed with headers: {}", exchange.getResponse().getHeaders())));
        };
    }

    //Lambda or Anonymous class - it does not matter
    //Even though it is anonymous class it will execute in the order of Order annotation
    @Order(2)
    @Bean
    public GlobalFilter myFifthGlobalFilter() {
        return new GlobalFilter() {
            @Override
            public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
                log.debug("My fifth Pre-Filter is executed with path {} and headers: {}", exchange.getRequest().getPath(), exchange.getRequest().getHeaders());
                return chain
                        .filter(exchange)
                        .then(Mono.fromRunnable(() -> log.debug("My fifth Post-Filter is executed with headers: {}", exchange.getResponse().getHeaders())));
            }
        };
    }
}
