package de.bentzin.ingwer.landfill.db;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

/**
 * @author Ture Bentzin
 * @since 2023-08-03
 */
public final class DatabaseConnector {

    private static final @NotNull Logger databaseConnectorLogger = LogManager.getLogger();

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    private final @NotNull Optional<String> instance;
    private final Logger logger;
    protected @Nullable SessionFactory database;

    public DatabaseConnector() {
        this.instance = Optional.empty(); //Default landfill db (stores the actual data)
        logger = databaseConnectorLogger;
    }

    public DatabaseConnector(@NotNull String instance) {
        logger = LogManager.getLogger(databaseConnectorLogger.getName() + "." + instance);
        logger.info("created instance for: " + instance);
        this.instance = Optional.of(instance);
    }

    public @NotNull SessionFactory getDatabase() {
        if (database == null) throw new IllegalStateException("SessionFactory is not available!");
        return database;
    }

    private @NotNull StandardServiceRegistryBuilder configureStandardServiceRegistry(@NotNull StandardServiceRegistryBuilder builder) {
        instance.ifPresentOrElse(s -> builder.configure(s + ".cfg.xml"), builder::configure);
        return builder;
    }

    public void setUp() {
        try (StandardServiceRegistry serviceRegistry =
                     configureStandardServiceRegistry(
                             new StandardServiceRegistryBuilder()
                     )
                             .build()
        ) {
            //Access to serviceRegistry
            try {
                logger.info("creating and updating database for operation...");
                database = new MetadataSources(serviceRegistry).buildMetadata().buildSessionFactory();
                logger.info("database is now up to date!");
            } catch (Exception exception) {
                logger.error("SessionFactory issue.. This needs to be addressed to maintain operational condition for landfill");
                StandardServiceRegistryBuilder.destroy(serviceRegistry);
                logger.throwing(exception);
                return;
            }

            /*


            try (Session session = database.openSession()) {
                Transaction transaction = session.beginTransaction();
                Data data0 = session.getReference(Data.class, 0);
                logger.info(data0 + " from DB");
                transaction.commit();
            }

             */

        }
    }

    public @NotNull Optional<String> getInstanceVariable() {
        return instance;
    }
}
