package account.common.config.jpa;


import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


@Configuration
@EnableJpaRepositories(basePackages = "com.example.user.domain.user.repository")
@EntityScan(basePackages = "com.example.user.domain.user")
public class JpaConfig {

}
