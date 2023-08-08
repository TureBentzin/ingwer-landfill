package de.bentzin.ingwer.landfill.db;

import org.junit.jupiter.api.Test;

/**
 * @author Ture Bentzin
 * @since 2023-08-03
 */
class DatabaseConnectorTest {

    private static DatabaseConnector databaseConnector = new DatabaseConnector();

    @Test
    void setUp() {
        databaseConnector.setUp();
    }

    @Test
    void connect() {
        databaseConnector.test();
    }
}