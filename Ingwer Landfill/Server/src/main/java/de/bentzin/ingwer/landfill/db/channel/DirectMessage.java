package de.bentzin.ingwer.landfill.db.channel;

import de.bentzin.ingwer.landfill.db.user.Account;
import jakarta.persistence.*;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Ture Bentzin
 * @since 2023-08-08
 */
@Entity
public class DirectMessage {

    @Id
    @OneToOne(optional = true)
    private @NotNull Channel channel;


    @OneToMany
    private @NotNull Set<DirectMessageMember> members = new HashSet<>();

    public DirectMessage(@NotNull Channel channel, Account @NotNull [] members) {
        this.channel = channel;
    }

    public DirectMessage() {
    }

    public @NotNull Channel getChannel() {
        return channel;
    }

    public void setChannel(@NotNull Channel channel) {
        this.channel = channel;
    }
}
