package de.bentzin.ingwer.landfill.netty;

import io.netty5.buffer.Buffer;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.zip.CRC32;

/**
 * @author Ture Bentzin
 * @since 2023-08-13
 */
public final class BufferUtils {

    public static void encodeString(@NotNull Buffer buffer, @NotNull String string) {
        byte[] bytes = string.getBytes(StandardCharsets.UTF_8);
        encodeBytes(buffer, bytes);
    }

    public static @NotNull String decodeString(@NotNull Buffer buffer) {
        return new String(decodeBytes(buffer));
    }

    public static void encodeURL(@NotNull Buffer buffer, @NotNull URL url) {
        String urlString = url.toString();
        encodeString(buffer, urlString);
    }

    @Contract("_ -> new")
    public static @NotNull URL decodeURL(@NotNull Buffer buffer) throws MalformedURLException {
        return new URL(decodeString(buffer));
    }

    public static void encodeUUID(@NotNull Buffer buffer, @NotNull UUID uuid) {
        String uuidString = uuid.toString();
        encodeString(buffer, uuidString);
    }

    @Contract("_ -> new")
    public static @NotNull UUID decodeUUID(@NotNull Buffer buffer) {
        return UUID.fromString(decodeString(buffer));
    }

    public static byte @NotNull [] decodeBytes(@NotNull Buffer buffer) {
        int length = buffer.readInt();
        return decodeBytes(buffer, length);
    }

    public static byte @NotNull [] decodeBytes(@NotNull Buffer buffer, int length) {
        byte[] bytes = new byte[length];
        buffer.readBytes(bytes, 0, length);
        return bytes;
    }

    public static void encodeBytes(@NotNull Buffer buffer, byte @NotNull [] bytes) {
        buffer.writeInt(bytes.length);
        buffer.writeBytes(bytes);
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

    public static long calculateChecksum(Buffer buffer) {
        Buffer read = buffer.copy();
        byte[] data = new byte[read.readableBytes()];
        read.readBytes(data, 0, data.length);

        CRC32 crc32 = new CRC32();
        crc32.update(data);
        return crc32.getValue();
    }

}
