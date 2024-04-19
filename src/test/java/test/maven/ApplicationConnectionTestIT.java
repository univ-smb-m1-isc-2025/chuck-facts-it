package test.maven;

import org.awaitility.Awaitility;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.event.annotation.BeforeTestClass;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.TimeUnit;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ApplicationConnectionTestIT {

    @Test
    public void testHealthCheckEndpoint() {

        // Hack until this is solved in .sh in a similar manner than postgres start-up
        Awaitility.await()
                .pollDelay(60 * 5, SECONDS)
                .atMost((60 * 5) + 1, SECONDS)
                .until(() -> true);

        RestTemplate template = new RestTemplate();

        String url = "http://localhost:8080/actuator/health";

        String response = template.getForObject(url, String.class);

        assertThat(response).contains("UP");

    }


}