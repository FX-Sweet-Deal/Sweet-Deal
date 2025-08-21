package account.common.config;

import javax.sql.DataSource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;

@Slf4j
@Configuration
@RequiredArgsConstructor
class DbProbe {
    private final DataSource dataSource;

    @EventListener(ApplicationReadyEvent.class)
    public void printCurrentCatalog() throws Exception {
        try (var c = dataSource.getConnection();
            var st = c.createStatement();
            var rs = st.executeQuery("SELECT DATABASE()")) {
            if (rs.next()) log.info(">>> CURRENT DB = {}", rs.getString(1));
        }
        try (var c = dataSource.getConnection();
            var st = c.createStatement();
            var rs = st.executeQuery("SELECT COUNT(*) FROM `user` WHERE id=10")) {
            rs.next();
            log.info(">>> COUNT user(id=10) = {}", rs.getLong(1));
        }
    }
}