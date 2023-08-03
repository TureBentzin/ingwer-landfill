package de.bentzin.ingwer.landfill;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

/**
 * Entrypoint of Application
 * @author Ture Bentzin
 * @since 2023-08-03
 */
public class Application {

    private static final @NotNull Logger logger = LogManager.getLogger();

    public static void main(String @NotNull [] args) {
        logger.info("Welcome to Ingwer Landfill!");
        LandfillServer.start();
        System.getProperties();
    }
}
