package de.bentzin.ingwer.landfill.netty.packet.put;

import io.netty5.buffer.Buffer;
import org.jetbrains.annotations.NotNull;

/**
 * @author Ture Bentzin
 * @since 2023-08-17
 */
public abstract sealed class PutChanneledPacket extends PutPacket permits PutGuildChannelPacket {

    private final long channelID;

    public PutChanneledPacket(@NotNull Buffer buffer) {
        super(buffer);
        channelID = buffer.readLong();
    }

    protected PutChanneledPacket(int jobID, long channelID) {
        super(jobID);
        this.channelID = channelID;
    }

    @Override
    protected final void encodePut(@NotNull Buffer buffer) {
        buffer.writeLong(channelID);
        encodeChanneledPut(buffer);
    }

    protected abstract void encodeChanneledPut(@NotNull Buffer buffer);

    public long getChannelID() {
        return channelID;
    }
}
