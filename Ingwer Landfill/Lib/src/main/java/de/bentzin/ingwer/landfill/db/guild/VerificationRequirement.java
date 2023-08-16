package de.bentzin.ingwer.landfill.db.guild;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

/**
 * @author Ture Bentzin
 * @since 2023-08-08
 */
public enum VerificationRequirement {

    /**
     * unrestricted
     */
    NONE(0),

    /**
     * must have verified email on account
     */
    LOW(1),

    /**
     * must be registered on Discord for longer than 5 minutes
     */
    MEDIUM(2),

    /**
     * must be a member of the server for longer than 10 minutes
     */
    HIGH(3),

    /**
     * must have a verified phone number
     */
    VERY_HIGH(4);


    private final int level;

    VerificationRequirement(int level) {
        this.level = level;
    }

    static @NotNull VerificationRequirement byLevel(int level) throws IllegalArgumentException {
        return Arrays.stream(values()).filter(verificationRequirement ->
                verificationRequirement.level == level).findFirst().orElseThrow(
                () -> new IllegalArgumentException("can´t find a VerificationRequirement for " + level));
    }

    public int getLevel() {
        return level;
    }
}
