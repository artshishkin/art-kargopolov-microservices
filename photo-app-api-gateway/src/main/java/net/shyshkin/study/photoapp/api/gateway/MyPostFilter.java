package net.shyshkin.study.photoapp.api.gateway;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class MyPostFilter implements GlobalFilter, Ordered {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        return chain.filter(exchange)

                .then(Mono.fromRunnable(() -> {
                    HttpHeaders headers = exchange.getResponse().getHeaders();
                    log.debug("My first Post-Filter is executed with headers: {}", headers);
                }));
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
