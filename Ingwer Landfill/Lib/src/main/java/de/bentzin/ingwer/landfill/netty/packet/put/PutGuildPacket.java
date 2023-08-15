package de.bentzin.ingwer.landfill.netty.packet.put;

import io.netty5.buffer.Buffer;
import org.jetbrains.annotations.NotNull;

/**
 * @author Ture Bentzin
 * @since 2023-08-15
 */
//TODO
public final class PutGuildPacket extends PutPacket {
    public PutGuildPacket(@NotNull Buffer buffer) {
        super(buffer);
    }

    public PutGuildPacket(int jobID) {
        super(jobID);
    }

    @Override
    public void encode(@NotNull Buffer buffer) {

    }
}
