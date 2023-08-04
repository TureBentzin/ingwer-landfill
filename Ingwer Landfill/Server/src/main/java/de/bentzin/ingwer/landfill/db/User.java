package de.bentzin.ingwer.landfill.db;

import de.bentzin.ingwer.landfill.Displayable;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Date;

/**
 * @author Ture Bentzin
 * @since 2023-08-04
 */
@Entity
@Table(name = "user")
public class User implements Displayable {

    /**
     * discord id
     */
    @Id
    private long id;

    /**
     * discord username (new name)
     */
    private @NotNull String username;

    /**
     * discord displayname
     */
    private @NotNull String displayname;

    private @NotNull Date joinDate;

    private @Nullable String legacyName;

    private @Nullable String pronouns;

    private @Nullable String aboutMe;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public @NotNull String getUsername() {
        return username;
    }

    public void setUsername(@NotNull String username) {
        this.username = username;
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

    @Override
    public @NotNull String toString() {
        return "User{" + "id=" + id +
                ", username='" + username + '\'' +
                ", displayname='" + displayname + '\'' +
                ", joinDate=" + joinDate +
                ", legacyName='" + legacyName + '\'' +
                ", pronouns='" + pronouns + '\'' +
                ", aboutMe='" + aboutMe + '\'' +
                '}';
    }
}
