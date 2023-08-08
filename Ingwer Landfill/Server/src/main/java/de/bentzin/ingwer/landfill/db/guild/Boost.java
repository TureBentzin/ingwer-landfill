package de.bentzin.ingwer.landfill.db.guild;


import de.bentzin.ingwer.landfill.db.user.Account;
import jakarta.persistence.*;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

/**
 * @author Ture Bentzin
 * @since 2023-08-08
 */
@Entity
@Table(name = "boost")
@IdClass(Boost.BoostPK.class)
public class Boost {

    @ManyToOne
    @Id
    private @NotNull Account account;

    @ManyToOne
    @Id
    private @NotNull Guild guild;

    private @NotNull Date since;


    public static class BoostPK implements Serializable {

        protected @NotNull Account account;

        protected @NotNull Guild guild;

        public BoostPK(@NotNull Account account, @NotNull Guild guild) {
            this.account = account;
            this.guild = guild;
        }

        public BoostPK() {

        }

        public @NotNull Account getAccount() {
            return account;
        }

        public void setAccount(@NotNull Account account) {
            this.account = account;
        }

        public @NotNull Guild getGuild() {
            return guild;
        }

        public void setGuild(@NotNull Guild guild) {
            this.guild = guild;
        }

        @Override
        public boolean equals(@NotNull Object o) {
            if (this == o) return true;
            if (!(o instanceof BoostPK boostPK)) return false;

            if (!Objects.equals(account, boostPK.account)) return false;
            return Objects.equals(guild, boostPK.guild);
        }

        @Override
        public int hashCode() {
            int result = account != null ? account.hashCode() : 0;
            result = 31 * result + (guild != null ? guild.hashCode() : 0);
            return result;
        }
    }

}
