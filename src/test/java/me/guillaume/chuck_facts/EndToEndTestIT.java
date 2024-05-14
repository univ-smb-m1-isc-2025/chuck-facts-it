package me.guillaume.chuck_facts;

import org.awaitility.Awaitility;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.web.client.RestTemplate;

import javax.sql.DataSource;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import me.guillaume.integration_testing.DBInitializer;

import static java.util.concurrent.TimeUnit.SECONDS;
import static me.guillaume.integration_testing.DBInitializer.run;
import static org.assertj.core.api.Assertions.assertThat;

// FIXME : define naming convention to link test to data
public class EndToEndTestIT {

    @BeforeAll
    public static void setUp() throws IOException {
        run("src/test/resources/EndToEndTestIT.pre.sql");
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
        run("src/test/resources/EndToEndTestIT.post.sql");
    }


}