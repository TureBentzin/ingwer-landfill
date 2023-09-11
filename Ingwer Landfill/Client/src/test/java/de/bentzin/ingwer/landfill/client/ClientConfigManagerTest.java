package de.bentzin.ingwer.landfill.client;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Ture Bentzin
 * @since 2023-08-13
 */
public class ClientConfigManagerTest {

    private static final @NotNull Logger logger = LogManager.getLogger();

    @Test
    public void test() {
        ClientConfigManager clientConfigManager = new ClientConfigManager();
        logger.info(clientConfigManager.getClientConf());
    }

    @Test
    public void saveTest() throws IOException {
        ClientConfigManager clientConfigManager = new ClientConfigManager();
        clientConfigManager.save();
    }

}