package de.bentzin.ingwer.landfill.db.user.badge;


import de.bentzin.ingwer.landfill.db.user.Account;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

/**
 * @author Ture Bentzin
 * @since 2023-08-08
 */
@Entity
@Table(name = "boost")
public class Boost {

    private Account account;

}
