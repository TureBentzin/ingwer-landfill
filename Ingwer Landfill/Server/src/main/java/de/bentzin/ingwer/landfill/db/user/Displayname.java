package de.bentzin.ingwer.landfill.db.user;

import jakarta.persistence.*;

import java.util.Date;

/**
 * @author Ture Bentzin
 * @since 2023-08-04
 */
@Entity
@Table(name = "displayname")
public class Displayname {

    @Id
    @GeneratedValue
    private long id;

    @ManyToOne
    private Account account;

    private String displayname;

    private Date firstRecorded = new Date();

    public Displayname(Account account, String displayname) {
        this.account = account;
        this.displayname = displayname;
    }

    public Displayname() {

    }

    public String getDisplayname() {
        return displayname;
    }

    public Account getAccount() {
        return account;
    }

    public Date getFirstRecorded() {
        return firstRecorded;
    }
}
