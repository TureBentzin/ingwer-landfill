package de.bentzin.ingwer.landfill.client;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Enumeration;

/**
 * @author Ture Bentzin
 * @since 2023-08-13
 */
public class ResourceAccessTest {

    private static final @NotNull Logger logger = LogManager.getLogger();

    @Test
    public void printAll() {
        // Get the ClassLoader
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

        try {
            // List all resources with the specified name (empty string to list all resources)
            Enumeration<URL> resources = classLoader.getResources("");

            while (resources.hasMoreElements()) {
                String file = resources.nextElement().getFile();
                logger.info("found Resource: " + file);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        logger.info("test completed!");
    }

    @Test
    public void clazzTest() {
        URL resource = getClass().getResource("client-default.toml");
        logger.info(resource);
    }
}
