package net.shyshkin.study.photoapp.api.gateway;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Mono;

@Slf4j
@Configuration
public class GlobalFilterConfiguration {

    @Bean
    public GlobalFilter mySecondGlobalFilter() {
        return (exchange, chain) -> {
            log.debug("My second Pre-Filter is executed with path {} and headers: {}", exchange.getRequest().getPath(), exchange.getRequest().getHeaders());
            return chain
                    .filter(exchange)
                    .then(Mono.fromRunnable(() -> log.debug("My second Post-Filter is executed with headers: {}", exchange.getResponse().getHeaders())));
        };
    }

    @Bean
    public GlobalFilter myThirdGlobalFilter() {
        return (exchange, chain) -> {
            log.debug("My third Pre-Filter is executed with path {} and headers: {}", exchange.getRequest().getPath(), exchange.getRequest().getHeaders());
            return chain
                    .filter(exchange)
                    .then(Mono.fromRunnable(() -> log.debug("My third Post-Filter is executed with headers: {}", exchange.getResponse().getHeaders())));
        };
    }

    @Bean
    public GlobalFilter myFourthGlobalFilter() {
        return (exchange, chain) -> {
            log.debug("My fourth Pre-Filter is executed with path {} and headers: {}", exchange.getRequest().getPath(), exchange.getRequest().getHeaders());
            return chain
                    .filter(exchange)
                    .then(Mono.fromRunnable(() -> log.debug("My fourth Post-Filter is executed with headers: {}", exchange.getResponse().getHeaders())));
        };
    }
}
