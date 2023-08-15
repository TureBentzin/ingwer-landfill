package de.bentzin.ingwer.landfill.db.user;

import de.bentzin.ingwer.landfill.db.cdn.ExternalReference;
import jakarta.persistence.*;
import org.jetbrains.annotations.NotNull;

/**
 * @author Ture Bentzin
 * @since 2023-08-15
 */
@Table
@Entity
public class Avatar {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;

    @ManyToOne
    private @NotNull Account account;

    @ManyToOne
    private @NotNull ExternalReference externalReference;

    public Avatar(@NotNull Account account, @NotNull ExternalReference externalReference) {
        this.account = account;
        this.externalReference = externalReference;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public @NotNull Account getAccount() {
        return account;
    }

    public void setAccount(@NotNull Account account) {
        this.account = account;
    }

    public @NotNull ExternalReference getExternalReference() {
        return externalReference;
    }

    public void setExternalReference(@NotNull ExternalReference externalReference) {
        this.externalReference = externalReference;
    }
}
