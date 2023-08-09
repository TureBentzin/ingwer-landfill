package de.bentzin.ingwer.landfill.db.user;

import de.bentzin.ingwer.landfill.db.DatabaseConnector;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.Test;


/**
 * @author Ture Bentzin
 * @since 2023-08-04
 */
public class UsernameTest {

    @Test
    public void test() {
        DatabaseConnector databaseConnector = new DatabaseConnector();
        databaseConnector.setUp();
        try (Session session = databaseConnector.getLandfillDB().openSession()) {
            Transaction transaction = session.beginTransaction();
            //Account account = new Account(467730889640640523L, List.of(),"Bommels05", new Date(),"Bommels#0000",null,null);
            Account account = session.byId(Account.class).getReference(467730889640640523L);
            account.logDynamicBoxedString();
            account.setId(467730889640640523L);
            account.setAboutMe("About MEEEE");
            account.logDynamicBoxedString();
            session.merge(account);
            account.logDynamicBoxedString();
            session.persist(new Usernames(account, "bommels05"));
            transaction.commit();
        }
    }

}