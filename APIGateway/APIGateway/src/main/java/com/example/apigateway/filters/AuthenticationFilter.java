package com.example.apigateway.filters;

import com.example.apigateway.service.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;


@Slf4j
@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {


    private final JwtService jwtService;
    public AuthenticationFilter(JwtService jwtService){
        super(Config.class);
        this.jwtService = jwtService;
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange,chain) -> {
            log.info("Login request: {}",exchange.getRequest().getURI());
            final String tokenHeader = exchange.getRequest().getHeaders().getFirst("Authorization");
            if (tokenHeader == null || !tokenHeader.startsWith("Bearer ")){
                log.error("Authorisation  Issue");
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete();
            }
            String jwtToken = tokenHeader.split("Bearer ")[1];
            String userId = jwtService.getUserIdFromToken(jwtToken);
            ServerWebExchange modifiedWebExchange = exchange
                    .mutate()
                    .request(r -> r.header("X-User-Id",userId))
                    .build();


            return chain.filter(modifiedWebExchange);
        };
    }

    public static class Config {

    }

}
