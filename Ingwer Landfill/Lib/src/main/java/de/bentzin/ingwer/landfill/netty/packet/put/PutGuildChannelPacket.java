package de.bentzin.ingwer.landfill.netty.packet.put;

import io.netty5.buffer.Buffer;
import org.jetbrains.annotations.NotNull;
import static de.bentzin.ingwer.landfill.netty.BufferUtils.*;

/**
 * @author Ture Bentzin
 * @since 2023-08-17
 */
public final class PutGuildChannelPacket extends PutChanneledPacket {

    private final long guildID;
    private final @NotNull String topic;

    public PutGuildChannelPacket(@NotNull Buffer buffer) {
        super(buffer);
        guildID = buffer.readLong();
        topic = decodeString(buffer);

    }

    public PutGuildChannelPacket(int jobID, long channelID, long guildID, @NotNull String topic) {
        super(jobID, channelID);
        this.guildID = guildID;
        this.topic = topic;
    }


    @Override
    protected void encodeChanneledPut(@NotNull Buffer buffer) {
        buffer.writeLong(guildID);
        encodeString(buffer, topic);
    }

    public long getGuildID() {
        return guildID;
    }

    public @NotNull String getTopic() {
        return topic;
    }
}
