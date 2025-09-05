package com.example.gateway.filter;

import com.example.gateway.common.exception.token.TokenException;
import com.example.gateway.user.model.TokenDto;
import com.example.gateway.user.model.TokenValidationRequest;
import com.example.gateway.user.model.TokenValidationResponse;
import com.example.global.errorCode.TokenErrorCode;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class ServiceApiPrivateFilter extends
    AbstractGatewayFilterFactory<ServiceApiPrivateFilter.Config> {

    public ServiceApiPrivateFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            // 요청 처리 전
            log.info("ServiceApiPrivateFilter Route Uri : {}", exchange.getRequest().getURI());

            // user server 를 통한 인증 실행
            // 1. 토큰의 유무
            List<String> headers = exchange.getRequest().getHeaders().get("Authorization");

            if (headers == null || headers.isEmpty()) {
                throw new TokenException(TokenErrorCode.NON_TOKEN_HEADER);
            }

            String token = headers.get(0);

            // 2. 토큰 유효성
            String userApiUri = UriComponentsBuilder
                .fromUriString("http://localhost") // URL 수정
                .port(8081)
                .path("/api/account") // 경로 추가
                .build()
                .encode()
                .toUriString();

            WebClient webClient = WebClient.builder()
                .baseUrl(userApiUri)
                .defaultHeader(HttpHeaders.AUTHORIZATION, token)
                .build();

            // TokenValidationRequest 객체 생성
            TokenValidationRequest request = new TokenValidationRequest(new TokenDto(token));

            return webClient.post()
                .body(BodyInserters.fromValue(request))
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .onStatus(
                    HttpStatusCode::isError,
                    response ->
                        response.bodyToMono(new ParameterizedTypeReference<Object>() {
                            })
                            .flatMap(error -> {
                                log.error("Error response: {}", error);
                                return Mono.error(new TokenException(TokenErrorCode.TOKEN_EXCEPTION));
                            })
                )
                .bodyToMono(new ParameterizedTypeReference<TokenValidationResponse>() {
                })
                .flatMap(response -> {
                    // 응답이 왔을 때
                    log.info("response : {}", response);

                    // 3. 사용자 정보 추가
                    String userId = response.getUserId() != null ? response.getUserId().toString() : null;
                    String email = response.getEmail() != null ? response.getEmail() : null;
                    String role = response.getRole() != null ? response.getRole().name() : null;

                    var proxyRequest = exchange.getRequest().mutate()
                        .header("x-user-id", userId)
                        .header("x-user-email", email)
                        .header("x-user-role", role)
                        .build();

                    var requestBuild = exchange.mutate().request(proxyRequest).build();

                    return chain.filter(requestBuild);
                })
                .onErrorMap(e -> {
                    log.error("", e);
                    return e;
                });
        };
    }

    public static class Config {

    }

}