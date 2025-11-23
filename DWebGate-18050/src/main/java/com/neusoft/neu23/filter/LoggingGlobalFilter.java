package com.neusoft.neu23.filter;


import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class LoggingGlobalFilter implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        long startTime = System.currentTimeMillis();

        String path = exchange.getRequest().getURI().getPath();
        String method = exchange.getRequest().getMethod().name();
        System.out.println("[GlobalFilter] Request start: " + method + " " + path);

        // 执行下游过滤器链
        return chain.filter(exchange).then(Mono.fromRunnable(() -> {
            long duration = System.currentTimeMillis() - startTime;
            System.out.println("[GlobalFilter] Request end: " + method + " " + path + " | Duration: " + duration + "ms");
        }));
    }

    @Override
    public int getOrder() {
        // 优先级：值越小越先执行
        return Ordered.HIGHEST_PRECEDENCE + 10;
    }
}
