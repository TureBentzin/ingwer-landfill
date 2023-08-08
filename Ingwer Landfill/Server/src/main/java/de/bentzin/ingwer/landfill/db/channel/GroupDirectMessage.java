package de.bentzin.ingwer.landfill.db.channel;

import de.bentzin.ingwer.landfill.db.user.Account;
import jakarta.persistence.*;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/**
 * @author Ture Bentzin
 * @since 2023-08-08
 */
@Entity
public class GroupDirectMessage {

    @Id
    @OneToOne(optional = true)
    private @NotNull Channel channel;

    @OneToMany
    private Set<GroupDirectMessageMember> members = new HashSet<>();

    @Lob
    @Basic
    @Column(name = "icon", columnDefinition = "bytea")
    private byte[] icon;

    public @NotNull Optional<Account> findOwner() {

        return members.stream().filter(GroupDirectMessageMember::isOwner).map(GroupDirectMessageMember::getAccount).findFirst();
    }



}
