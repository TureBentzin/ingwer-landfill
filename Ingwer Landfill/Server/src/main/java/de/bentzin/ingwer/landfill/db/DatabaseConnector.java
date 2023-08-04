package de.bentzin.ingwer.landfill.db;

import de.bentzin.ingwer.landfill.db.test.Data;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.util.Assert;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author Ture Bentzin
 * @since 2023-08-03
 */
public class DatabaseConnector {

    private static final @NotNull Logger logger = LogManager.getLogger();

    protected @Nullable SessionFactory sessionFactory;

    public @NotNull SessionFactory getSessionFactory() {
        if(sessionFactory == null) throw new IllegalStateException("SessionFactory is not available!");
        return sessionFactory;
    }

    public void setUp() {
        try(StandardServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                .configure()
                .build()
        ) {
            //Access to serviceRegistry
            try {
                sessionFactory = new MetadataSources(serviceRegistry).buildMetadata().buildSessionFactory();
            } catch (Exception exception){
                StandardServiceRegistryBuilder.destroy(serviceRegistry);
                logger.throwing(exception);
                return;
            }

            try (Session session = sessionFactory.openSession()) {
                Transaction transaction = session.beginTransaction();
                Data data0 = session.getReference(Data.class, 0);
                logger.info(data0 + " from DB");
                transaction.commit();
            }

        }
    }

    public void connect() {

    }
}
