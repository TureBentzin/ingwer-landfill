package de.bentzin.ingwer.landfill.netty.packet;

import de.bentzin.ingwer.landfill.Displayable;
import de.bentzin.ingwer.landfill.netty.Packet;
import io.netty5.buffer.Buffer;
import org.jetbrains.annotations.NotNull;

import java.nio.charset.StandardCharsets;

/**
 * Send by the client after connection is established to introduce himself without being asked to do so
 *
 * @author Ture Bentzin
 * @since 2023-08-13
 */
public class KnockKnockPacket implements Packet, Displayable {

    private final @NotNull String workerID;

    private final long time;

    public KnockKnockPacket(@NotNull String workerID) {
        this.workerID = workerID;
        time = System.currentTimeMillis();
    }

    public KnockKnockPacket(Buffer buffer) {
        int length = buffer.readInt();
        time = buffer.readLong();
        byte[] data = new byte[length];
        buffer.readBytes(data, 0,length);
        workerID = new String(data, StandardCharsets.UTF_8);
    }


    @Override
    public void encode(@NotNull Buffer buffer) {
        byte[] bytes = workerID.getBytes(StandardCharsets.UTF_8);
        buffer.writeInt(bytes.length);//length
        buffer.writeLong(time); //data test
        buffer.writeBytes(bytes); //workerID

    }

    @Override
    public @NotNull String toString() {
        return workerID;
    }

    public @NotNull String getWorkerID() {
        return workerID;
    }

    public long getTime() {
        return time;
    }
}
