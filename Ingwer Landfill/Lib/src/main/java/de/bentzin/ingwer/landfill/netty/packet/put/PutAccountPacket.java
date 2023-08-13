package de.bentzin.ingwer.landfill.netty.packet.put;

import de.bentzin.ingwer.landfill.netty.BufferUtils;
import io.netty5.buffer.Buffer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static de.bentzin.ingwer.landfill.netty.BufferUtils.*;

/**
 * @author Ture Bentzin
 * @since 2023-08-13
 */
public class PutAccountPacket extends PutPacket {

    private final long id;
    private final @NotNull String userName;
    private final @NotNull String displayName;
    private final long joinDate;
    private final @Nullable String legacyName;
    private final @Nullable String pronouns;
    private final @Nullable String aboutMe;

    public PutAccountPacket(@NotNull Buffer buffer) {
        super(buffer);
        id = buffer.readLong();
        userName = decodeString(buffer);
        displayName = decodeString(buffer);
        joinDate = buffer.readLong();
        legacyName = decodeNullable(buffer, BufferUtils::decodeString);
        pronouns = decodeNullable(buffer, BufferUtils::decodeString);
        aboutMe = decodeNullable(buffer, BufferUtils::decodeString);

    }

    @Override
    public void encode(@NotNull Buffer buffer) {
        superEncode(buffer);
        buffer.writeLong(id);
        encodeString(buffer, userName);
        encodeString(buffer, displayName);
        buffer.writeLong(joinDate);
        encodeNullable(buffer, displayName, BufferUtils::encodeString);
        encodeNullable(buffer, pronouns, BufferUtils::encodeString);
        encodeNullable(buffer, aboutMe, BufferUtils::encodeString);
    }

    public long getId() {
        return id;
    }

    public @NotNull String getUserName() {
        return userName;
    }

    public @NotNull String getDisplayName() {
        return displayName;
    }

    public long getJoinDate() {
        return joinDate;
    }

    public @Nullable String getLegacyName() {
        return legacyName;
    }

    public @Nullable String getPronouns() {
        return pronouns;
    }

    public @Nullable String getAboutMe() {
        return aboutMe;
    }
}
