package de.bentzin.ingwer.landfill.netty;

import io.netty5.buffer.Buffer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.nio.charset.StandardCharsets;
import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * @author Ture Bentzin
 * @since 2023-08-13
 */
public final class BufferUtils {

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

    public static <T> void encodeNullable(@NotNull Buffer buffer,
                                          @Nullable T value,
                                          @NotNull BiConsumer<@NotNull Buffer, @NotNull T> encoder) {
        if (value == null) {
            buffer.writeByte((byte) 0);
        } else {
            buffer.writeByte((byte) 1);
            encoder.accept(buffer, value);
        }
    }

    public static <T> @Nullable T decodeNullable(@NotNull Buffer buffer, @NotNull Function<@NotNull Buffer, @NotNull T> decoder) {
        if (buffer.readByte() == 1) {
            return decoder.apply(buffer);
        }
        return null;
    }
}
