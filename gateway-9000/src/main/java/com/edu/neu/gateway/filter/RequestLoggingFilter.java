package com.edu.neu.gateway.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.UUID;

@Slf4j
@Component
public class RequestLoggingFilter implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String traceId = UUID.randomUUID().toString();
        exchange.getRequest().mutate().header("X-Trace-Id", traceId).build();
        URI requestUri = exchange.getRequest().getURI();
        log.info("[Gateway] traceId={} method={} uri={}", traceId, exchange.getRequest().getMethod(), requestUri);
        return chain.filter(exchange)
                .doOnSuccess(unused -> log.info("[Gateway] traceId={} completed", traceId));
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }
}


