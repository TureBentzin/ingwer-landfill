package de.bentzin.ingwer.landfill;

import de.bentzin.ingwer.landfill.db.DatabaseConnector;
import de.bentzin.ingwer.landfill.properties.PropertyUtils;
import de.bentzin.ingwer.landfill.service.Server;
import io.netty5.util.NettyRuntime;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.jetbrains.annotations.NotNull;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.security.Security;

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
        if(PropertyUtils.SYSTEM.checkForProperty("landfill.started"))
            throw new IllegalStateException("Landfill seems to have already started in this VM!");
        logger.info("check completed successfully! Proceeding with boot...");
        System.setProperty("landfill.started", "1");

        DatabaseConnector databaseConnector = new DatabaseConnector();
        databaseConnector.setUp();
        databaseConnector.test();

        //Setup Security (BC - SSL)
        if(Security.getProvider("BC") == null) {
            Security.addProvider(new BouncyCastleProvider());
        }

        Server server = Server.create(new InetSocketAddress("::1", 2222));
        logger.info("starting netty server...");
        server.start();
        logger.info("server started...");

    }
}
