package de.bentzin.ingwer.landfill.netty.packet;

import de.bentzin.ingwer.landfill.netty.Packet;
import io.netty5.buffer.Buffer;
import org.jetbrains.annotations.NotNull;

import java.nio.charset.StandardCharsets;

/**
 * @author Ture Bentzin
 * 01.08.2023
 */
public class StringPacket implements Packet {

    public StringPacket(@NotNull String string) {
        this.string = string;
    }

    public StringPacket(@NotNull Buffer buffer) {
        int length = buffer.readInt();
        byte[] data = new byte[length];
        buffer.readBytes(data, 0,length);
        string = new String(data, StandardCharsets.UTF_8);
    }

    private final @NotNull String string;

    @Override
    public void encode(@NotNull Buffer buffer) {
        byte[] bytes = string.getBytes(StandardCharsets.UTF_8);
        buffer.writeInt(bytes.length); //length
        buffer.writeBytes(bytes);
    }

    @Override
    public String toString() {
        return string;
    }
}
