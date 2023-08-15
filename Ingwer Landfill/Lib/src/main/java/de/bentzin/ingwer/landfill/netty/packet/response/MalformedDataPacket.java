package de.bentzin.ingwer.landfill.netty.packet.response;

import de.bentzin.ingwer.landfill.netty.BufferUtils;
import io.netty5.buffer.Buffer;
import io.netty5.buffer.BufferUtil;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * @author Ture Bentzin
 * @since 2023-08-15
 */
public final class MalformedDataPacket extends ResponsePacket {

    private final @NotNull String execption;

    public MalformedDataPacket(@NotNull String execption) {
        super(ResponseType.MALFORMED);
        this.execption = execption;
    }

    public MalformedDataPacket(@NotNull Buffer buffer) {
        super(buffer);
        execption = BufferUtils.decodeString(buffer);
    }

    @Override
    public void encode(@NotNull Buffer buffer) {
        superEncode(buffer);
        BufferUtils.encodeString(buffer, execption);
    }
}
