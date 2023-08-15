package de.bentzin.ingwer.landfill.db.user;

import de.bentzin.ingwer.landfill.Displayable;
import de.bentzin.ingwer.landfill.DisplaynameField;
import de.bentzin.ingwer.landfill.db.guild.Guild;
import de.bentzin.ingwer.landfill.db.guild.GuildMembership;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import org.hibernate.Session;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Ture Bentzin
 * @since 2023-08-04
 */
@Entity
@Table(name = "account")
public class Account implements Displayable {

    /**
     * discord id
     */
    @Id
    private long id;

    @OneToMany(mappedBy = "account")
    private @NotNull List<Username> usernames;

    /**
     * discord displayname
     */
    @DisplaynameField
    private @NotNull String displayname;

    private @NotNull Date joinDate;

    private @Nullable String legacyName;

    private @Nullable String pronouns;

    private @Nullable String aboutMe;

    private boolean bot;

    public Account(long id, @NotNull List<Username> usernames, @NotNull String displayname, @NotNull Date joinDate, @Nullable String legacyName, @Nullable String pronouns, @Nullable String aboutMe, @NotNull boolean bot) {
        this.id = id;
        this.usernames = usernames;
        this.displayname = displayname;
        this.joinDate = joinDate;
        this.legacyName = legacyName;
        this.pronouns = pronouns;
        this.aboutMe = aboutMe;
        this.bot = bot;
    }

    public Account() {

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }


    public @NotNull String getDisplayname() {
        return displayname;
    }

    public void setDisplayname(@NotNull String displayname) {
        this.displayname = displayname;
    }

    public @NotNull Date getJoinDate() {
        return joinDate;
    }

    public void setJoinDate(@NotNull Date joinDate) {
        this.joinDate = joinDate;
    }

    public @Nullable String getLegacyName() {
        return legacyName;
    }

    public void setLegacyName(@Nullable String legacyName) {
        this.legacyName = legacyName;
    }

    public @Nullable String getPronouns() {
        return pronouns;
    }

    public void setPronouns(@Nullable String pronouns) {
        this.pronouns = pronouns;
    }

    public @Nullable String getAboutMe() {
        return aboutMe;
    }

    public void setAboutMe(@Nullable String aboutMe) {
        this.aboutMe = aboutMe;
    }

    public @NotNull List<Username> getUsernames() {
        return usernames;
    }

    public void currentUsername(@NotNull String username, @NotNull Session session) {
        if(getUsernames().stream().map(Username::getUsername).noneMatch(s -> s.equals(username))) {
            Username usernames = new Username(this, username);
            session.persist(usernames);
            getUsernames().add(usernames);
        }
    }

    public boolean isBot() {
        return bot;
    }

    public void setBot(boolean bot) {
        this.bot = bot;
    }

    @SuppressWarnings("NotNullFieldNotInitialized")
    @OneToMany(mappedBy = "account")
    private @NotNull Collection<Avatar> avatars;

    public @NotNull Collection<Avatar> getAvatars() {
        return avatars;
    }

    @OneToMany(mappedBy = "account")
    @SuppressWarnings("NotNullFieldNotInitialized")
    private @NotNull Collection<GuildMembership> guildMemberships;

    public @NotNull Collection<GuildMembership> getGuildMemberships() {
        return guildMemberships;
    }

    public @NotNull Collection<Guild> getGuilds() {
        return guildMemberships.stream().map(GuildMembership::getGuild).collect(Collectors.toUnmodifiableSet());
    }
}
