package de.bentzin.ingwer.landfill.db.user;

import de.bentzin.ingwer.landfill.Displayable;
import de.bentzin.ingwer.landfill.DisplaynameField;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Date;
import java.util.List;

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
    private List<Usernames> usernames;

    /**
     * discord displayname
     */
    @DisplaynameField
    private @NotNull String displayname;

    private @NotNull Date joinDate;

    private @Nullable String legacyName;

    private @Nullable String pronouns;

    private @Nullable String aboutMe;

    public Account(long id, List<Usernames> usernames, @NotNull String displayname, @NotNull Date joinDate, @Nullable String legacyName, @Nullable String pronouns, @Nullable String aboutMe) {
        this.id = id;
        this.usernames = usernames;
        this.displayname = displayname;
        this.joinDate = joinDate;
        this.legacyName = legacyName;
        this.pronouns = pronouns;
        this.aboutMe = aboutMe;
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

}
