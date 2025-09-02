package com.example.gateway.filter;

import com.example.gateway.common.exception.token.TokenException;
import com.example.gateway.user.model.TokenDto;
import com.example.gateway.user.model.TokenValidationRequest;
import com.example.gateway.user.model.TokenValidationResponse;
import com.example.global.errorcode.TokenErrorCode;

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

            // 들어온 요청의 전체 URI 로그
            log.info("ServiceApiPrivateFilter Route Uri : {}", exchange.getRequest().getURI());

            // user server 를 통한 인증 실행
            // 토큰의 유무 검사. 없으면 즉시 예외로 종료
            List<String> headers = exchange.getRequest().getHeaders().get("Authorization");
            if (headers == null || headers.isEmpty()) {
                throw new TokenException(TokenErrorCode.NON_TOKEN_HEADER);
            }
            String token = headers.get(0);

            // 검증을 호출할 사용자 서버의 엔드포인트를 안전하게 구성
            String userApiUri = UriComponentsBuilder
                .fromUriString("http://localhost") // URL 수정
                .port(8081)
                .path("/api/account") // 경로 추가
                .build()
                .encode()
                .toUriString();

            // 비동기/논블로킹 HTTP 클라이언트(WebClient) 준비
            // 검증 서버에도 원본 Authorization 헤더를 전달
            WebClient webClient = WebClient.builder()
                .baseUrl(userApiUri)
                .defaultHeader(HttpHeaders.AUTHORIZATION, token)
                .build();

            // 바디에도 토큰을 담는 검증 요청 DTO를 구성 (헤더 + 바디 둘다 전달하는 설계)
            TokenValidationRequest request = new TokenValidationRequest(new TokenDto(token));

            // 검증 API 호출(POST, JSON)
            return webClient.post()
                .body(BodyInserters.fromValue(request))
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()

                // HTTP 4xx/ 5xx 일 때의 에러 매핑
                // 바디를 로그로 남기고, 도메인 예외로 변환해 upstream으로 에러 신호 전파
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

                // 성공(2xx) 이면 응답을 TokenValidationResponse 로 디시리얼라이즈함
                .bodyToMono(new ParameterizedTypeReference<TokenValidationResponse>() {
                })
                .flatMap(response -> {
                    // 응답이 왔을 때
                    log.info("response : {}", response);

                    // 검증 응답에서 사용자 식별정보 추출
                    String userId = response.getUserId() != null ? response.getUserId().toString() : null;
                    String email = response.getEmail() != null ? response.getEmail() : null;
                    String role = response.getRole() != null ? response.getRole().name() : null;

                   // 헤더 주입 후 전달
                    var proxyRequest = exchange.getRequest().mutate()
                        .header("x-user-id", userId)
                        .header("x-user-email", email)
                        .header("x-user-role", role)
                        .build();
                    var requestBuild = exchange.mutate().request(proxyRequest).build();

                    log.info("requestBuild: {}", requestBuild);

                    log.info("chain.filter(requestBuild) {}", chain.filter(requestBuild));
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