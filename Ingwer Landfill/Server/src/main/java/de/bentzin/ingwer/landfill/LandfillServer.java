package de.bentzin.ingwer.landfill;

import de.bentzin.ingwer.landfill.db.DatabaseConnector;
import de.bentzin.ingwer.landfill.properties.PropertyUtils;
import de.bentzin.ingwer.landfill.service.Server;
import de.bentzin.ingwer.landfill.tasks.TaskManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Closeable;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.security.Security;
import java.util.Objects;
import java.util.Scanner;
import java.util.function.Supplier;

/**
 * @author Ture Bentzin
 * @since 2023-08-03
 */
public class LandfillServer implements Runnable, Closeable {

    private static final @NotNull Logger logger = LogManager.getLogger();

    @NotNull
    public static final LandfillServer LANDFILL_SERVER = new LandfillServer();
    private @Nullable TaskManager taskmanager;
    private @Nullable DatabaseConnector databaseConnector;
    private Server server;

    public static @NotNull LandfillServer start() throws IllegalStateException{
        LANDFILL_SERVER.run();
        return LANDFILL_SERVER;
    }

    @Override
    public void run() throws IllegalStateException{
        logger.info("checking for \"landfill.started\"...");
        if(PropertyUtils.SYSTEM.checkForProperty("landfill.started"))
            throw new IllegalStateException("Landfill seems to have already started in this VM!");
        logger.info("check completed successfully! Proceeding with boot...");
        System.setProperty("landfill.started", "1");

        databaseConnector = new DatabaseConnector();
        databaseConnector.setUp();

        //Setup Security (BC - SSL)
        if(Security.getProvider("BC") == null) {
            Security.addProvider(new BouncyCastleProvider());
        }

        server = Server.create(new InetSocketAddress("::1", 2222));
        logger.info("starting netty server...");
        server.start();
        logger.info("server started...");
        logger.info("starting taskmanager...");
        taskmanager = new TaskManager();

        logger.info("listening for \"close\" temporary keyword in System.in");
        //REMOVE BEFORE PR

        final Scanner scanner = new Scanner(System.in);

        while (true) {
            String input = scanner.nextLine();
            if ("close".equalsIgnoreCase(input)) {
                break;
            }
        }

        try {
            close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public @Nullable DatabaseConnector getDatabaseConnector() {
        return databaseConnector;
    }

    public @Nullable TaskManager getTaskmanager() {
        return taskmanager;
    }

    @Override
    public void close() throws IOException {
        //close netty first
        try {
            server.stop();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        //wait for TaskManager to finish
        Supplier<Integer> size = () -> Objects.requireNonNull(taskmanager, "TaskManager was never started or is no longer available!").viewQueue().size();
        logger.info("Taskmanager has " + size + " remaining tasks! Starting to wait for completion of those by " + Objects.requireNonNull(taskmanager, "TaskManager was never started or is no longer available!").getExecutorGroup().activeCount() + " ExecutorThreads!");
        while (size.get() > 0) {
            try {
                Thread.sleep(1000); //processor load management
                logger.info("Taskmanager has " + size + " remaining tasks!");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        //shutdown taskmanager
        taskmanager.close();

        //shutdown hibernate
        Objects.requireNonNull(databaseConnector, "Hibernate was never started or is no longer available!").getLandfillDB().close();

    }
}
