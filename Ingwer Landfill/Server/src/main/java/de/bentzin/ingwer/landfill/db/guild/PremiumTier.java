package de.bentzin.ingwer.landfill.db.guild;

/**
 * @author Ture Bentzin
 * @since 2023-08-08
 */
public enum PremiumTier {

    NONE(0),

    TIER_1(1),

    TIER_2(2),

    TIER(3)

    ;
    private final int tier;

    PremiumTier(int tier) {
        this.tier = tier;
    }

    public int getTier() {
        return tier;
    }

    public boolean hasPremium() {
        return getTier() > 0;
    }
}
