package de.bentzin.ingwer.landfill.netty.packet.put;

import de.bentzin.ingwer.landfill.db.guild.GuildNSFWLevel;
import de.bentzin.ingwer.landfill.db.guild.PremiumTier;
import de.bentzin.ingwer.landfill.db.guild.VerificationRequirement;
import de.bentzin.ingwer.landfill.netty.BufferUtils;
import io.netty5.buffer.Buffer;
import org.jetbrains.annotations.NotNull;

import static de.bentzin.ingwer.landfill.netty.BufferUtils.*;


public final class PutGuildPacket extends PutPacket {

    private final long id;
    private final @NotNull String name;
    private final @NotNull String description;
    private final long ownerID;
    private final @NotNull VerificationRequirement verificationRequirement;
    private final @NotNull PremiumTier premiumTier;
    private final @NotNull GuildNSFWLevel guildNSFWLevel;


    public PutGuildPacket(@NotNull Buffer buffer) {
        super(buffer);
        id = buffer.readLong();
        name = BufferUtils.decodeString(buffer);
        description = decodeString(buffer);
        ownerID = buffer.readLong();
        verificationRequirement = decodeEnum(buffer, VerificationRequirement.class);
        premiumTier = decodeEnum(buffer, PremiumTier.class);
        guildNSFWLevel = decodeEnum(buffer, GuildNSFWLevel.class);
    }

    public PutGuildPacket(int jobID, long id, @NotNull String name, @NotNull String description, long ownerID, @NotNull VerificationRequirement verificationRequirement, @NotNull PremiumTier premiumTier, @NotNull GuildNSFWLevel guildNSFWLevel) {
        super(jobID);
        this.id = id;
        this.name = name;
        this.description = description;
        this.ownerID = ownerID;
        this.verificationRequirement = verificationRequirement;
        this.premiumTier = premiumTier;
        this.guildNSFWLevel = guildNSFWLevel;
    }

    @Override
    public void encode(@NotNull Buffer buffer) {
        superEncode(buffer);
        buffer.writeLong(id);
        encodeString(buffer, name);
        encodeString(buffer, description);
        buffer.writeLong(ownerID);
        encodeEnum(buffer, verificationRequirement);
        encodeEnum(buffer, premiumTier);
        encodeEnum(buffer, guildNSFWLevel);
    }

    public long getId() {
        return id;
    }

    public @NotNull String getName() {
        return name;
    }

    public long getOwnerID() {
        return ownerID;
    }

    public @NotNull VerificationRequirement getVerificationRequirement() {
        return verificationRequirement;
    }

    public @NotNull PremiumTier getPremiumTier() {
        return premiumTier;
    }

    public @NotNull GuildNSFWLevel getGuildNSFWLevel() {
        return guildNSFWLevel;
    }

    public String getDescription() {
        return description;
    }
}
