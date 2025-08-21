package account.common.config.securiy;


import account.domain.account.service.TokenService;
import account.domain.security.filter.JwtAuthFilter;
import account.domain.security.service.AuthorizationService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.EnableGlobalAuthentication;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalAuthentication
@RequiredArgsConstructor
public class SecurityConfig {

    private final AuthenticationEntryPoint authEntryPoint;
    private final TokenService tokenService;
    private final AuthorizationService authorizationService;

    private final List<String> WHITE_LIST = List.of("/swagger-ui.html", "/swagger-ui/**",
        "/v3/api-docs/**", "/open-api/**",
        "/api/account");

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.cors(cors -> cors.disable())
            .addFilterBefore(new JwtAuthFilter(tokenService, authorizationService),
                UsernamePasswordAuthenticationFilter.class)
            .csrf((csrfConfig) -> csrfConfig.disable())
            .sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(
                SessionCreationPolicy.STATELESS)).authorizeHttpRequests(it -> {
                it.requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                    .requestMatchers(WHITE_LIST.toArray(new String[0])).permitAll().anyRequest()
                    .authenticated()
                ;
            }).formLogin(AbstractHttpConfigurer::disable).httpBasic(AbstractHttpConfigurer::disable)
            .httpBasic(basic -> basic.authenticationEntryPoint(authEntryPoint))
            .exceptionHandling(Customizer.withDefaults());

        return httpSecurity.build();
    }

    //    @Bean
    public BCryptPasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().requestMatchers(WHITE_LIST.toArray(new String[0]));
    }

}