package de.bentzin.ingwer.landfill.netty.packet.put;

import de.bentzin.ingwer.landfill.db.channel.ChannelType;
import de.bentzin.ingwer.landfill.netty.BufferUtils;
import de.bentzin.ingwer.landfill.netty.packet.put.PutPacket;
import io.netty5.buffer.Buffer;
import org.jetbrains.annotations.NotNull;

import static de.bentzin.ingwer.landfill.netty.BufferUtils.*;

/**
 * @author Ture Bentzin
 * @since 2023-08-17
 */
public final class PutChannelPacket extends PutPacket {

    private final long id;

    public long getId() {
        return id;
    }

    public @NotNull ChannelType getChannelType() {
        return channelType;
    }

    public @NotNull String getName() {
        return name;
    }

    private final @NotNull ChannelType channelType;
    private final @NotNull String name;

    public PutChannelPacket(int jobID, long id, @NotNull ChannelType channelType, @NotNull String name) {
        super(jobID);
        this.id = id;
        this.channelType = channelType;
        this.name = name;
    }

    public PutChannelPacket(@NotNull Buffer buffer) {
        super(buffer);
        id = buffer.readLong();
        channelType = decodeEnum(buffer, ChannelType.class);
        name = decodeString(buffer);
    }

    @Override
    public void encodePut(@NotNull Buffer buffer) {
        buffer.writeLong(id);
        encodeEnum(buffer, channelType);
        encodeString(buffer, name);
    }
}
