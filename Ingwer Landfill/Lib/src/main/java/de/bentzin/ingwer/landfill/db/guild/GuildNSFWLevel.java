package de.bentzin.ingwer.landfill.db.guild;

/**
 * @author Ture Bentzin
 * @since 2023-08-08
 */
public enum GuildNSFWLevel {

    DEFAULT(0),
    EXPLICIT(1),
    SAFE(2),
    AGE_RESTRICTED(3);


    private final int level;

    GuildNSFWLevel(int level) {
        this.level = level;
    }

    public int getLevel() {
        return level;
    }
}
