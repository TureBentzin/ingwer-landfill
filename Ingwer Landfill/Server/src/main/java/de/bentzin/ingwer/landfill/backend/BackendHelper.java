package de.bentzin.ingwer.landfill.backend;

import de.bentzin.ingwer.landfill.LandfillServer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.List;
import java.util.Set;

/**
 * @author Ture Bentzin
 * @since 2023-08-24
 */
public class BackendHelper {

    private static final @NotNull Logger logger = LogManager.getLogger();

    private BackendHelper() {

    }

    /**
     * @return the public keys or an empty Set
     */
    @Deprecated
    public static @Unmodifiable @NotNull Set<CharSequence> getAllPublicKeys() {
        return getAllPublicKeys(LandfillServer.LANDFILL_SERVER);
    }

    public static @Unmodifiable @NotNull Set<AuthorizedDumptruckOperator> allAuthorizedOperators() {
        return allAuthorizedOperators(LandfillServer.LANDFILL_SERVER);
    }

    public static @Unmodifiable @NotNull Set<AuthorizedDumptruckOperator> allAuthorizedOperators(@NotNull LandfillServer landfillServer) {
        Session session = landfillServer.getBackendDatabaseConnector().getDatabase().openSession();
        Transaction transaction = session.beginTransaction();
        try {
            Query<AuthorizedDumptruckOperator> fromUser = session.createQuery("FROM AuthorizedDumptruckOperator"); //bad code - hard reference to AuthorizedDumptruckOperator
            List<AuthorizedDumptruckOperator> list = fromUser.getResultList();
            return Set.copyOf(list);
        }catch (ClassCastException e) {
            logger.error("HQL ERROR: " + e);
            logger.throwing(e);
        }
        return Set.of();
    }

    /**
     * @param landfillServer the server
     * @return the public keys or an empty Set
     */
    @Deprecated
    public static @Unmodifiable @NotNull Set<CharSequence> getAllPublicKeys(@NotNull LandfillServer landfillServer) {
        Session session = landfillServer.getBackendDatabaseConnector().getDatabase().openSession();
        Transaction transaction = session.beginTransaction();
        try {
            Query<AuthorizedDumptruckOperator> fromUser = session.createQuery("FROM AuthorizedDumptruckOperator"); //bad code - hard reference to AuthorizedDumptruckOperator
            List<String> list = fromUser.getResultList().stream().map(AuthorizedDumptruckOperator::getPublicKey).toList();
            return Set.copyOf(list);
        }catch (ClassCastException e) {
            logger.error("HQL ERROR: " + e);
            logger.throwing(e);
        }
        return Set.of();
    }
}
