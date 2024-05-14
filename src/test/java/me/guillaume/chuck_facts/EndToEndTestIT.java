package me.guillaume.chuck_facts;

import org.awaitility.Awaitility;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.web.client.RestTemplate;

import javax.sql.DataSource;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.assertj.core.api.Assertions.assertThat;

// FIXME : define naming convention to link test to data
public class EndToEndTestIT {

    // FIXME : remove duplication by providing a utility that extract following from docker docker compose
    private final String username = "postgres";
    private final String jdbcUrl = "jdbc:postgresql://localhost:5432/postgres";
    private final String password = "password";

    @BeforeAll
    public void setUp() throws IOException {

        DataSource dataSource = new DriverManagerDataSource(jdbcUrl, username, password);
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

        String sql = new String(Files.readAllBytes(Paths.get("src/test/resources/EndToEndTestIT.pre.sql")));
        for (String statement : sql.split(";")) {
            if (!statement.trim().isEmpty()) {
                jdbcTemplate.execute(statement.trim());
            }
        }
    }

    @Test
    public void testHealthCheckEndpoint() {

        // FIXME : Hack until this is solved in .sh in a similar manner than postgres start-up
        Awaitility.await()
                .pollDelay(60 * 5, SECONDS)
                .atMost((60 * 5) + 1, SECONDS)
                .until(() -> true);

        RestTemplate template = new RestTemplate();

        String url = "https://chuck.oups.net/api/chuck-facts";

        String response = template.getForObject(url, String.class);

        assertThat(response).contains("Chuck Norris doesn’t do push-ups. He pushes the Earth down.");

    }

    @AfterAll
    public void tearDown() throws IOException {

        DataSource dataSource = new DriverManagerDataSource(jdbcUrl, username, password);
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

        String sql = new String(Files.readAllBytes(Paths.get("src/test/resources/EndToEndTestIT.post.sql")));
        for (String statement : sql.split(";")) {
            if (!statement.trim().isEmpty()) {
                jdbcTemplate.execute(statement.trim());
            }
        }
    }


}