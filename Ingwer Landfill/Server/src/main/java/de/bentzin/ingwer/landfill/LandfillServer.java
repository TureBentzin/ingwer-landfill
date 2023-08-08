package de.bentzin.ingwer.landfill;

import de.bentzin.ingwer.landfill.db.DatabaseConnector;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

/**
 * @author Ture Bentzin
 * @since 2023-08-03
 */
public class LandfillServer implements Runnable {

    private static final @NotNull Logger logger = LogManager.getLogger();

    @NotNull
    public static final LandfillServer LANDFILL_SERVER = new LandfillServer();

    public static @NotNull LandfillServer start() throws IllegalStateException{
        LANDFILL_SERVER.run();
        return LANDFILL_SERVER;
    }

    @Override
    public void run() throws IllegalStateException{
        logger.info("checking for \"landfill.started\"...");
        if(System.getProperties().containsKey("landfill.started"))
            throw new IllegalStateException("Landfill seems to have already started in this VM!");
        logger.info("check completed successfully! Proceeding with boot...");
        System.setProperty("landfill.started", "1");

        DatabaseConnector databaseConnector = new DatabaseConnector();
        databaseConnector.setUp();
        databaseConnector.test();

    }
}
