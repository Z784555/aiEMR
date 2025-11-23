package com.neusoft.neu23.filter;


import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class CorsGlobalFilter implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        HttpHeaders headers = exchange.getResponse().getHeaders();
//        headers.add("Access-Control-Allow-Origin", "*");
//        headers.add("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
//        headers.add("Access-Control-Allow-Headers", "Content-Type, Authorization, X-Token");
//        headers.add("Access-Control-Max-Age", "18000");
//        headers.add("Access-Control-Allow-Credentials", "false");

        headers.add("MyToken", "" + System.currentTimeMillis() );

        // 如果是预检请求 (OPTIONS)，直接返回 200
        if (exchange.getRequest().getMethod() == HttpMethod.OPTIONS) {
            exchange.getResponse().setStatusCode(HttpStatus.OK);
            return exchange.getResponse().setComplete();
        }

        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        // 优先级必须足够高，确保在其他过滤器之前运行
        return Ordered.HIGHEST_PRECEDENCE;
    }
}
