package de.bentzin.ingwer.landfill.netty.packet.response;

import io.netty5.buffer.Buffer;
import org.jetbrains.annotations.NotNull;

/**
 * @author Ture Bentzin
 * @since 2023-08-25
 */
public final class WelcomePacket extends ResponsePacket {
    public WelcomePacket() {
        super(Re);
    }

    public WelcomePacket(@NotNull Buffer buffer) {
        super(buffer);
    }

    @Override
    public void encode(@NotNull Buffer buffer) {

    }
}
