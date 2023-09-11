package de.bentzin.ingwer.landfill.netty.packet.response;

import de.bentzin.ingwer.landfill.netty.BufferUtils;
import io.netty5.buffer.Buffer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.Collection;
import java.util.List;

import static de.bentzin.ingwer.landfill.netty.BufferUtils.*;

/**
 * @author Ture Bentzin
 * @since 2023-08-25
 */
public final class WelcomePacket extends ResponsePacket {

    private final @NotNull String identifier;
    private final @NotNull Collection<String> activePrivileges;


    public WelcomePacket(@NotNull Buffer buffer) {
        super(buffer);
        identifier = decodeString(buffer);
        activePrivileges = decodeCollection(buffer, BufferUtils::decodeString);
    }

    @Override
    public void encode(@NotNull Buffer buffer) {
        encodeString(buffer, identifier);
        encodeCollection(buffer, activePrivileges,  BufferUtils::encodeString);
    }

    public @NotNull String getIdentifier() {
        return identifier;
    }

    public @NotNull @Unmodifiable List<String> getActivePrivileges() {
        return List.copyOf(activePrivileges);
    }

}
