package de.bentzin.ingwer.landfill.db.user.badge;

import org.jetbrains.annotations.NotNull;

/**
 * Client representation used here
 * @author Ture Bentzin
 * @since 2023-08-08
 */
public enum BadgeType {

    LEGACY_USERNAME,

    /**
     * Nitro
     */
    PREMIUM,

    /**
     * Bravery
     */
    HYPESQUAD_HOUSE_1,

    /**
     * Brilliance
     */
    HYPESQUAD_HOUSE_2,

    /**
     * Balance
     */
    HYPESQUAD_HOUSE_3,

    /**
     * Hypesquad Events (Participant)
     */
    HYPESQUAD,

    GUILD_BOOSTER_LVL1,

    GUILD_BOOSTER_LVL2,

    GUILD_BOOSTER_LVL3,

    GUILD_BOOSTER_LVL4,

    GUILD_BOOSTER_LVL5,

    GUILD_BOOSTER_LVL6,

    ACTIVE_DEVELOPER,

    VERIFIED_DEVELOPER,

    BOT_COMMANDS,

    /**
     * Automod (for Bots)
     */
    AUTOMOD,

    STAFF,

    CERTIFIED_MODERATOR,

    EARLY_SUPPORTED,

    PARTNER,



    ;

    public @NotNull String toID() {
        return this.name().toLowerCase();
    }
}
