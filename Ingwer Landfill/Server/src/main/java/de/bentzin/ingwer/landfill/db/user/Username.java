package de.bentzin.ingwer.landfill.db.user;

import jakarta.persistence.*;

import java.util.Date;

/**
 * @author Ture Bentzin
 * @since 2023-08-04
 */
@Entity
@Table(name = "username")
public class Username {

    @Id
    @GeneratedValue
    private long id;

    @ManyToOne
    private Account account;

    private String username;

    private Date firstRecorded = new Date();

    public Username(Account account, String username) {
        this.account = account;
        this.username = username;
    }

    public Username() {

    }

    public String getUsername() {
        return username;
    }

    public Account getAccount() {
        return account;
    }

    public Date getFirstRecorded() {
        return firstRecorded;
    }
}
