package de.bentzin.ingwer.landfill.db.channel;

import de.bentzin.ingwer.landfill.db.user.Account;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.ManyToOne;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

/**
 * @author Ture Bentzin
 * @since 2023-08-08
 */
@Entity
@IdClass(DirectMessageMember.DirectMessageMemberPK.class)
public class DirectMessageMember {

    @Id
    @ManyToOne
    public @NotNull DirectMessage directMessage;

    @Id
    @ManyToOne
    private @NotNull Account account;


    public DirectMessageMember(@NotNull Account account) {
        this.account = account;
    }


    public DirectMessageMember() {
    }

    public @NotNull Account getAccount() {
        return account;
    }

    public void setAccount(@NotNull Account account) {
        this.account = account;
    }

    public static class DirectMessageMemberPK implements Serializable {
        @Id
        protected @NotNull DirectMessage directMessage;

        @Id
        protected @NotNull Account account;

        public DirectMessageMemberPK(@NotNull DirectMessage directMessage, @NotNull Account account) {
            this.directMessage = directMessage;
            this.account = account;
        }

        public DirectMessageMemberPK() {
        }

        public @NotNull DirectMessage getDirectMessage() {
            return directMessage;
        }

        public void setGroupDirectMessage(DirectMessage directMessage) {
            this.directMessage = directMessage;
        }

        public @NotNull Account getAccount() {
            return account;
        }

        public void setAccount(@NotNull Account account) {
            this.account = account;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof DirectMessageMemberPK that)) return false;

            if (!getDirectMessage().equals(that.getDirectMessage())) return false;
            return getAccount().equals(that.getAccount());
        }

        @Override
        public int hashCode() {
            int result = getDirectMessage().hashCode();
            result = 31 * result + getAccount().hashCode();
            return result;
        }
    }
}
