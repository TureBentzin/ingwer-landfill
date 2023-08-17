package de.bentzin.ingwer.landfill.db.guild;

import de.bentzin.ingwer.landfill.db.user.Account;
import jakarta.persistence.*;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

/**
 * @author Ture Bentzin
 * @since 2023-08-08
 */
@Entity
@Table(name = "guild_membership")
@IdClass(GuildMembership.GuildMembershipPK.class)
public class GuildMembership {

    @Id
    @ManyToOne
    private @NotNull Account account;

    @Id
    @ManyToOne
    private @NotNull Guild guild;

    public GuildMembership(@NotNull Account account, @NotNull Guild guild) {
        this.account = account;
        this.guild = guild;
    }

    public GuildMembership() {

    }

    public @NotNull Account getAccount() {
        return account;
    }

    public @NotNull Guild getGuild() {
        return guild;
    }

    public static class GuildMembershipPK implements Serializable {
        protected @NotNull Account account;

        protected @NotNull Guild guild;

        public GuildMembershipPK(@NotNull Account account, @NotNull Guild guild) {
            this.account = account;
            this.guild = guild;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof GuildMembershipPK that)) return false;

            if (!account.equals(that.account)) return false;
            return guild.equals(that.guild);
        }

        @Override
        public int hashCode() {
            int result = account.hashCode();
            result = 31 * result + guild.hashCode();
            return result;
        }
    }
}
