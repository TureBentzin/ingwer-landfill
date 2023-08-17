package de.bentzin.ingwer.landfill;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;

/**
 * Entrypoint of Application
 * @author Ture Bentzin
 * @since 2023-08-03
 */
public class Application {

    private static final @NotNull Logger logger = LogManager.getLogger();

    public static void main(String @NotNull [] args) throws IOException {
        logger.info("Welcome to Ingwer Landfill!");
        //noinspection EmptyTryBlock
        try(LandfillServer landfillServer = LandfillServer.start()) {
            // remains running because close console
        }
        //logger.info("Execution: Loggertest");
        //loggerTest(logger);
    }

    private static void loggerTest(@NotNull Logger logger) {
        for (Level value : Arrays.stream(Level.values()).sorted(Comparator.comparingInt(Level::intLevel)).toList()) {
            logger.log(value, value.name() + " | " + value.intLevel());
        }
    }
}
