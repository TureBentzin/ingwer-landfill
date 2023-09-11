package de.bentzin.ingwer.landfill.db.user.badge;

import de.bentzin.ingwer.landfill.db.user.Account;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;

/**
 * @author Ture Bentzin
 * @since 2023-08-06
 */
@Entity
@Table(name = "badges")
public class BadgeOwnership {

    @Id
    private @NotNull BadgeType type;

    @Id
    @ManyToOne
    private @NotNull Account account;

    private @Nullable String raw;

    public @NotNull BadgeType getType() {
        return type;
    }

    public void setType(@NotNull BadgeType type) {
        this.type = type;
    }

    public @NotNull Account getAccount() {
        return account;
    }

    public void setAccount(@NotNull Account account) {
        this.account = account;
    }

    public @Nullable String getRaw() {
        return raw;
    }

    public void setRaw(@Nullable String raw) {
        this.raw = raw;
    }

    public static class BadgeOwnershipPK implements Serializable{

        protected @NotNull BadgeType type;

        protected @NotNull Account account;

        public BadgeOwnershipPK() {

        }

        public BadgeOwnershipPK(@NotNull BadgeType type, @NotNull Account account) {
            this.type = type;
            this.account = account;
        }

        public @NotNull BadgeType getType() {
            return type;
        }

        public void setType(@NotNull BadgeType type) {
            this.type = type;
        }

        public @NotNull Account getAccount() {
            return account;
        }

        public void setAccount(@NotNull Account account) {
            this.account = account;
        }

        @Override
        public boolean equals(@NotNull Object o) {

            if (this == o) return true;
            if (!(o instanceof BadgeOwnershipPK that)) return false;

            if (type != that.type) return false;
            return account.equals(that.account);
        }

        @Override
        public int hashCode() {
            int result = type.hashCode();
            result = 31 * result + account.hashCode();
            return result;
        }
    }
}
