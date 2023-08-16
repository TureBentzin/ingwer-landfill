package de.bentzin.ingwer.landfill.db.user;

import de.bentzin.ingwer.landfill.db.cdn.ExternalReference;
import jakarta.persistence.*;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

/**
 * @author Ture Bentzin
 * @since 2023-08-15
 */
@Table
@Entity
@IdClass(Avatar.AvatarPK.class)
public class Avatar {

    @Column(unique = true)
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;

    @Id
    @ManyToOne
    private @NotNull Account account;

    @Id
    @ManyToOne
    private @NotNull ExternalReference externalReference;

    public Avatar(@NotNull Account account, @NotNull ExternalReference externalReference) {
        this.account = account;
        this.externalReference = externalReference;
    }

    public Avatar() {

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

    public static class AvatarPK implements Serializable {
        protected @NotNull Account account;

        protected @NotNull ExternalReference externalReference;

        public AvatarPK(@NotNull Account account, @NotNull ExternalReference externalReference) {
            this.account = account;
            this.externalReference = externalReference;
        }

        public AvatarPK() {
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

        @Override
        public boolean equals(@NotNull Object o) {
            if (this == o) return true;
            if (!(o instanceof AvatarPK avatarPK)) return false;

            if (!getAccount().equals(avatarPK.getAccount())) return false;
            return getExternalReference().equals(avatarPK.getExternalReference());
        }

        @Override
        public int hashCode() {
            int result = getAccount().hashCode();
            result = 31 * result + getExternalReference().hashCode();
            return result;
        }
    }
}
