package com.neusoft.neu23.filter;


import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class RouteLoggingGatewayFilterFactory extends AbstractGatewayFilterFactory<RouteLoggingGatewayFilterFactory.Config> {

    public RouteLoggingGatewayFilterFactory() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            // 前置逻辑
            String path = exchange.getRequest().getURI().getPath();
            System.out.println("[局部 -- >RouteFilter] Request path: " + path + ", message: " + config.getMessage());

            // 执行下游过滤器链
            return chain.filter(exchange).then(Mono.fromRunnable(() -> {
                // 后置逻辑
                System.out.println("[局部 -- > RouteFilter] Response completed for path: " + path);
            }));
        };
    }
    @Setter
    @Getter
    @Data
    public static class Config {
        private String message;

    }
}
