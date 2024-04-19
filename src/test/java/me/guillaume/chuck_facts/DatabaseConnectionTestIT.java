package me.guillaume.chuck_facts;


import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;


public class DatabaseConnectionTestIT {

    private Connection connection;
    private final String jdbcUrl = "jdbc:postgresql://localhost:5432/postgres";
    
    // FIXME : remove duplication with docker compose
    private final String username = "postgres";
    private final String password = "password";

    @BeforeEach
    public void setUp() throws Exception {
        try {
            connection = DriverManager.getConnection(jdbcUrl, username, password);
        } catch (SQLException e) {
            e.printStackTrace();
            fail("Failed to connect to the database");
        }
    }

    @AfterEach
    public void tearDown() throws Exception {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    public void testConnection() {
        assertNotNull(connection, "Connection should not be null");
        try {
            assertTrue(connection.isValid(5), "Connection should be valid");
        } catch (SQLException e) {
            e.printStackTrace();
            fail("Failed to check connection validity");
        }
    }
}
