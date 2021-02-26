package net.shyshkin.study.photoapp.api.gateway;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class MyPreFilter implements GlobalFilter {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        log.debug("My first pre-filter is executed");
        String requestPath = exchange.getRequest().getPath().toString();
        log.debug("Request path: {}", requestPath);
        HttpHeaders headers = exchange.getRequest().getHeaders();
        headers.forEach((key, value) -> log.debug("{} : {}", key, value));
        return chain.filter(exchange);
    }
}
