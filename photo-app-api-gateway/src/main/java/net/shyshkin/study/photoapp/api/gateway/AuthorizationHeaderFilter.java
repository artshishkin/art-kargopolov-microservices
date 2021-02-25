package net.shyshkin.study.photoapp.api.gateway;

import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@Component
@Slf4j
public class AuthorizationHeaderFilter extends AbstractGatewayFilterFactory<AuthorizationHeaderFilter.Config> {

    @Value("${token.secret}")
    private String tokenSecret;

    public AuthorizationHeaderFilter() {
        super(Config.class);
    }

    public static class Config {
        // Put configuration properties here
    }

    @Override
    public GatewayFilter apply(Config config) {

        GatewayFilter gatewayFilter = new GatewayFilter() {
            @Override
            public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
                ServerHttpRequest request = exchange.getRequest();

                if (!request.getHeaders().containsKey(AUTHORIZATION)) {
                    return onError(exchange, "No authorization header", UNAUTHORIZED);
                }
                List<String> authorizationList = request.getHeaders().get(AUTHORIZATION);
                String jwt = authorizationList.get(0).replace("Bearer ", "");

                if (!isJwtValid(jwt)) return onError(exchange, "JWT not valid", UNAUTHORIZED);

                return chain.filter(exchange);
            }
        };
        return gatewayFilter;
    }

    private Mono<Void> onError(ServerWebExchange exchange, String err, HttpStatus httpStatus) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(httpStatus);
        return response.setComplete();
    }

    private boolean isJwtValid(String token) {
        String subject = Jwts
                .parser()
                .setSigningKey(tokenSecret)
                .parseClaimsJwt(token)
                .getBody()
                .getSubject();
        return StringUtils.hasText(subject);
    }
}
