package me.guillaume.integration_testing;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class DBInitializer {

    // FIXME : remove duplication by providing a utility that extract following from docker docker compose
    private static final String username = "postgres";
    private static final String jdbcUrl = "jdbc:postgresql://localhost:5432/postgres";
    private static final String password = "password";

    public static void run(String first) throws IOException {
        var dataSource = new DriverManagerDataSource(jdbcUrl, username, password);
        var jdbcTemplate = new JdbcTemplate(dataSource);

        var sql = new String(Files.readAllBytes(Paths.get(first)));
        for (String statement : sql.split(";")) {
            if (!statement.trim().isEmpty()) {
                jdbcTemplate.execute(statement.trim());
            }
        }
    }

}
