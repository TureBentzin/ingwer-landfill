package de.bentzin.ingwer.landfill.db.guild;

import de.bentzin.ingwer.landfill.Displayable;
import de.bentzin.ingwer.landfill.DisplaynameField;
import de.bentzin.ingwer.landfill.db.user.Account;
import jakarta.persistence.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author Ture Bentzin
 * @since 2023-08-08
 */
@Entity
@Table(name = "guild")
public class Guild implements Displayable {

    /*
    Server Owner? y
    Regions?

     */

    @Id
    private long id;

    @DisplaynameField
    private @NotNull String name;

    private @Nullable String description;

    @ManyToOne
    private @NotNull Account owner;

    @Enumerated(EnumType.STRING)
    private @Nullable VerificationRequirement verificationRequirement;

    @Enumerated(EnumType.STRING)
    private @NotNull PremiumTier premiumTier;

    @Enumerated(EnumType.STRING)
    private @Nullable GuildNSFWLevel nsfwLevel;

    public Guild() {
    }

    public Guild(@NotNull String name, @Nullable String description, @NotNull Account owner, @NotNull VerificationRequirement verificationRequirement, @NotNull PremiumTier premiumTier, @NotNull GuildNSFWLevel nsfwLevel) {
        this.name = name;
        this.description = description;
        this.owner = owner;
        this.verificationRequirement = verificationRequirement;
        this.premiumTier = premiumTier;
        this.nsfwLevel = nsfwLevel;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public @NotNull String getName() {
        return name;
    }

    public void setName(@NotNull String name) {
        this.name = name;
    }

    public @NotNull Account getOwner() {
        return owner;
    }

    public void setOwner(@NotNull Account owner) {
        this.owner = owner;
    }

    public @Nullable VerificationRequirement getVerificationRequirement() {
        return verificationRequirement;
    }

    public void setVerificationRequirement(@NotNull VerificationRequirement verificationRequirement) {
        this.verificationRequirement = verificationRequirement;
    }

    public @NotNull PremiumTier getPremiumTier() {
        return premiumTier;
    }

    public void setPremiumTier(@NotNull PremiumTier premiumTier) {
        this.premiumTier = premiumTier;
    }

    public @Nullable GuildNSFWLevel getNsfwLevel() {
        return nsfwLevel;
    }

    public void setNsfwLevel(@NotNull GuildNSFWLevel nsfwLevel) {
        this.nsfwLevel = nsfwLevel;
    }

    public @Nullable String getDescription() {
        return description;
    }

    public void setDescription(@Nullable String description) {
        this.description = description;
    }
}
