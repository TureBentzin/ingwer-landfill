package de.bentzin.ingwer.landfill.netty.packet.response;

import io.netty5.buffer.Buffer;
import org.jetbrains.annotations.NotNull;

import static de.bentzin.ingwer.landfill.netty.BufferUtils.decodeString;
import static de.bentzin.ingwer.landfill.netty.BufferUtils.encodeString;

/**
 * @author Ture Bentzin
 * @since 2023-08-25
 */
public final class WelcomePacket extends ResponsePacket {

    private final @NotNull String identifier;


    public WelcomePacket(@NotNull Buffer buffer) {
        super(buffer);
        identifier = decodeString(buffer);
    }

    @Override
    public void encode(@NotNull Buffer buffer) {
        encodeString(buffer, identifier);
    }

    public @NotNull String getIdentifier() {
        return identifier;
    }
}
