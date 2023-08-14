package de.bentzin.ingwer.landfill.netty.packet.response;

import de.bentzin.ingwer.landfill.netty.Packet;
import io.netty5.buffer.Buffer;
import org.jetbrains.annotations.NotNull;

/**
 * @author Ture Bentzin
 * @since 2023-08-14
 */
public abstract sealed class ResponsePacket implements Packet permits PutConfirmResponsePacket {

    protected final @NotNull ResponseType responseType;

    protected ResponsePacket(@NotNull ResponseType responseType) {
        this.responseType = responseType;
    }

    protected ResponsePacket(@NotNull Buffer buffer) {
        responseType = ResponseType.values()[buffer.readInt()];
    }

    protected void superEncode(@NotNull Buffer buffer) {
        buffer.writeInt(responseType.ordinal());
    }


    public enum ResponseType {
        UNKNOWN(ResponsePacket.class),
        PUT_CONFIRM(PutConfirmResponsePacket.class);

        private @NotNull Class<? extends ResponsePacket> packetClazz;

        ResponseType(@NotNull Class<? extends ResponsePacket> packetClazz) {
            this.packetClazz = packetClazz;
        }
    }

    public @NotNull ResponseType getResponseType() {
        return responseType;
    }
}
