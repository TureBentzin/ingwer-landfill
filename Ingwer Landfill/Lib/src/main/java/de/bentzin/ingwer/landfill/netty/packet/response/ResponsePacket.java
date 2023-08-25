package de.bentzin.ingwer.landfill.netty.packet.response;

import de.bentzin.ingwer.landfill.netty.Packet;
import de.bentzin.ingwer.landfill.netty.packet.put.PutPacket;
import io.netty5.buffer.Buffer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.TestOnly;

import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Optional;

/**
 * @author Ture Bentzin
 * @since 2023-08-14
 */
public abstract sealed class ResponsePacket implements Packet permits MalformedDataPacket, PutConfirmResponsePacket, WelcomePacket {

    protected final @NotNull ResponseType responseType;

    @TestOnly
    private ResponsePacket(@NotNull ResponseType responseType) {
        this.responseType = responseType;
    }

    protected ResponsePacket() {
        this.responseType = findType().orElseThrow(() -> new NoSuchElementException(getClass().getSimpleName() + " seems to have no associated ResponseType!"))
    }

    protected ResponsePacket(@NotNull Buffer buffer) {
        responseType = ResponseType.values()[buffer.readInt()];
    }

    protected void superEncode(@NotNull Buffer buffer) {
        buffer.writeInt(responseType.ordinal());
    }


    public enum ResponseType {
        UNKNOWN(ResponsePacket.class),
        MALFORMED(MalformedDataPacket.class),
        PUT_CONFIRM(PutConfirmResponsePacket.class),
        WELCOME(WelcomePacket.class),
        ;

        private final @NotNull Class<? extends ResponsePacket> packetClass;

        ResponseType(@NotNull Class<? extends ResponsePacket> packetClass) {
            this.packetClass = packetClass;
        }

        public @NotNull Class<? extends ResponsePacket> getPacketClass() {
            return packetClass;
        }

        public boolean check(@NotNull Object o) {
            return getPacketClass().isInstance(o);
        }

        public static @NotNull Optional<ResponsePacket.ResponseType> findType(@NotNull Class<? extends ResponsePacket> packetClass) {
            return Arrays.stream(values()).filter(datatype1 -> datatype1.getPacketClass() == packetClass).findFirst();
        }

    }

    protected @NotNull Optional<ResponseType> findType() {
        return ResponseType.findType(getClass());
    }

    public @NotNull ResponseType getResponseType() {
        return responseType;
    }
}
