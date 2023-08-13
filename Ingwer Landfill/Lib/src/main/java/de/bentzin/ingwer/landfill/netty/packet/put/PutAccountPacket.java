package de.bentzin.ingwer.landfill.netty.packet.put;

import io.netty5.buffer.Buffer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author Ture Bentzin
 * @since 2023-08-13
 */
public class PutAccountPacket extends PutPacket{

    private long id;
    private @NotNull String userName;
    private @NotNull String displayName;
    private long joinDate;
    private @Nullable String legacyName;
    private @Nullable String pronouns;
    private @Nullable String aboutMe;

    public PutAccountPacket(@NotNull Buffer buffer) {
        super(buffer);

    }

    @Override
    public void encode(@NotNull Buffer buffer) {
        // superEncode(buffer, );
    }
}
