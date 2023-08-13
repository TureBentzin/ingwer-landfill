package de.bentzin.ingwer.landfill.netty;

import io.netty5.buffer.Buffer;
import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

/**
 * @author Ture Bentzin
 * @since 2023-08-13
 */
public class BufferUtils {

    public static void encodeString(@NotNull Buffer buffer, @NotNull String string) {
        byte[] bytes = string.getBytes(StandardCharsets.UTF_8);
        buffer.writeInt(bytes.length);
        buffer.writeBytes(bytes);
    }

    public static @NotNull String decodeString(@NotNull Buffer buffer) {
        int length = buffer.readInt();
        return new String(decodeBytes(buffer, length));
    }

    public static byte @NotNull [] decodeBytes(@NotNull Buffer buffer, int length) {
        byte[] bytes = new byte[length];
        buffer.readBytes(bytes, 0, length);
        return bytes;
    }
}
