package de.bentzin.ingwer.landfill.db.user;

import jakarta.persistence.*;
import org.jetbrains.annotations.NotNull;

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
    private @NotNull Account account;

    private @NotNull String displayname;

    private @NotNull Date firstRecorded = new Date();

    public Displayname(@NotNull Account account, @NotNull String username) {
        this.account = account;
        this.displayname = username;
    }

    public Displayname() {

    }

    public @NotNull String getDisplayname() {
        return displayname;
    }

    public @NotNull Account getAccount() {
        return account;
    }

    public @NotNull Date getFirstRecorded() {
        return firstRecorded;
    }
}
