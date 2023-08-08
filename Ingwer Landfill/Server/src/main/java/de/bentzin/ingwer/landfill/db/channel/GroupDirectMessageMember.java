package de.bentzin.ingwer.landfill.db.channel;

import de.bentzin.ingwer.landfill.db.user.Account;
import jakarta.persistence.*;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

/**
 * @author Ture Bentzin
 * @since 2023-08-08
 */
@Entity
@IdClass(GroupDirectMessageMember.GroupDirectMessageMemberPK.class)
public class GroupDirectMessageMember {

    @Id
    @ManyToOne
    public @NotNull GroupDirectMessage groupDirectMessage;

    @Id
    @ManyToOne
    private @NotNull Account account;

    private boolean owner = false;

    public GroupDirectMessageMember(@NotNull Account account) {
        this.account = account;
    }

    public GroupDirectMessageMember(@NotNull Account account, boolean owner) {
        this.account = account;
        this.owner = owner;
    }

    public GroupDirectMessageMember() {
    }

    public boolean isOwner() {
        return owner;
    }

    public void setOwner(boolean owner) {
        this.owner = owner;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public static class GroupDirectMessageMemberPK implements Serializable {
        @Id
        protected @NotNull GroupDirectMessage groupDirectMessage;

        @Id
        protected @NotNull Account account;

        public GroupDirectMessageMemberPK(@NotNull GroupDirectMessage groupDirectMessage, @NotNull Account account) {
            this.groupDirectMessage = groupDirectMessage;
            this.account = account;
        }

        public GroupDirectMessageMemberPK() {
        }

        public @NotNull GroupDirectMessage getGroupDirectMessage() {
            return groupDirectMessage;
        }

        public void setGroupDirectMessage(@NotNull GroupDirectMessage groupDirectMessage) {
            this.groupDirectMessage = groupDirectMessage;
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
            if (!(o instanceof GroupDirectMessageMemberPK that)) return false;

            if (!getGroupDirectMessage().equals(that.getGroupDirectMessage())) return false;
            return getAccount().equals(that.getAccount());
        }

        @Override
        public int hashCode() {
            int result = getGroupDirectMessage().hashCode();
            result = 31 * result + getAccount().hashCode();
            return result;
        }
    }
}
