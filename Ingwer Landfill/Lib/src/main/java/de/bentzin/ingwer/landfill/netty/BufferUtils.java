package de.bentzin.ingwer.landfill.netty;

import io.netty5.buffer.Buffer;
import org.jetbrains.annotations.*;

import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Collectors;
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

    public static <T extends Enum<T>> @NotNull T decodeEnum(@NotNull Buffer buffer, @NotNull Class<T> enumClazz) {
        return enumClazz.getEnumConstants()[buffer.readInt()];
    }

    public static <T extends Enum<T>> void encodeEnum(@NotNull Buffer buffer, @NotNull T t) {
        buffer.writeInt(t.ordinal());
    }

    public static <T> void encodeCollection(@NotNull Buffer buffer, @NotNull Collection<@NotNull T> collection,
                                            @NotNull BiConsumer<@NotNull Buffer, @NotNull T> encoder) {
        encodeIterable(buffer, collection, encoder, Collection::size);
    }

    public static <T> @NotNull Collection<T> decodeCollection(@NotNull Buffer buffer, @NotNull Function<@NotNull Buffer,
            @NotNull T> decoder) {
        int size = buffer.readInt();
        ArrayList<T> arrayList = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            arrayList.add(decoder.apply(buffer));
        }
        return arrayList;
    }

    //Compatible with encodeIterable
    public static <T> void encodeArray(@NotNull Buffer buffer, T @NotNull [] ts, @NotNull BiConsumer<@NotNull Buffer, @NotNull T> encoder) {
        buffer.writeInt(ts.length);
        for (T t : ts) {
            encoder.accept(buffer, t);
        }
    }

    /**
     * Use {@link #decodeCollection(Buffer, Function)} and {@link Collection#toArray()} instead
     * @param buffer buffer
     * @param decoder decoder
     * @return array containing the decoded Ts as Objects
     * @param <T> Type
     */
    @Deprecated
    public static <T> Object @NotNull [] decodeArray(@NotNull Buffer buffer,@NotNull Function<@NotNull Buffer, @NotNull T> decoder) {
        Object[] objects = new Object[buffer.readInt()];
        for (int i = 0; i < objects.length; i++) {
            objects[i] = decoder.apply(buffer);
        }
        return objects;
    }

    @ApiStatus.Internal
    public static <T, I extends Iterable<T>> void encodeIterable(@NotNull Buffer buffer, @NotNull I iterable, @NotNull BiConsumer<@NotNull Buffer, @NotNull T> encoder,
                                                                 @NotNull Function<I, Integer> sizeMapper) {
        buffer.writeInt(sizeMapper.apply(iterable));
        for (T t : iterable) {
            encoder.accept(buffer, t);
        }
    }


    public static <K, V> void encodeMap(@NotNull Buffer buffer, @NotNull Map<K, V> map, @NotNull BiConsumer<@NotNull Buffer,
            @NotNull K> kEncoder, @NotNull BiConsumer<@NotNull Buffer, @NotNull V> vEncoder) {
        encodeCollection(buffer, map.entrySet(), (buffer1, entry) -> encodeEntry(buffer1, entry, kEncoder, vEncoder));
    }

    public static <K, V> @NotNull @Unmodifiable Map<K, V> decodeMap(@NotNull Buffer buffer,
                                                                    @NotNull Function<@NotNull Buffer, @NotNull K> kDecoder,
                                                                    @NotNull Function<@NotNull Buffer, @NotNull V> vDecoder) {
        return decodeCollection(buffer, buffer1 -> decodeEntry(buffer1, kDecoder, vDecoder)).stream()
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    public static <K, V> void encodeEntry(@NotNull Buffer buffer, Map.@NotNull Entry<@NotNull K, @NotNull V> entry,
                                          @NotNull BiConsumer<@NotNull Buffer, @NotNull K> kEncoder,
                                          @NotNull BiConsumer<@NotNull Buffer, @NotNull V> vEncoder) {
        kEncoder.accept(buffer, entry.getKey());
        vEncoder.accept(buffer, entry.getValue());
    }

    @Contract("_, _, _ -> new")
    public static <K, V> Map.@NotNull @Unmodifiable Entry<K, V> decodeEntry(@NotNull Buffer buffer, @NotNull Function<@NotNull Buffer, @NotNull K> kDecoder, @NotNull Function<@NotNull Buffer, @NotNull V> vDecoder) {
        return Map.entry(
                kDecoder.apply(buffer),
                vDecoder.apply(buffer)
        );
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
