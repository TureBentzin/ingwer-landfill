package de.bentzin.ingwer.landfill.netty.packet.put;

import io.netty5.buffer.Buffer;
import org.jetbrains.annotations.NotNull;

import java.net.MalformedURLException;
import java.net.URL;

import static de.bentzin.ingwer.landfill.netty.BufferUtils.*;

/**
 * @author Ture Bentzin
 * @since 2023-08-15
 */
public final class PutAvatarPacket extends PutPacket{

    private final long account;
    private final @NotNull URL reference;

    public PutAvatarPacket(@NotNull Buffer buffer) {
        super(buffer);
        account = buffer.readLong();
        try {
            reference = decodeURL(buffer);
        } catch (MalformedURLException e) {
            throw new RuntimeException("malformed buffer",e);
        }

    }

    public PutAvatarPacket(int jobID, long account, @NotNull URL reference) {
        super(jobID);
        this.account = account;
        this.reference = reference;
    }

    @Override
    public void encodePut(@NotNull Buffer buffer) {
        buffer.writeLong(account);
        encodeURL(buffer, reference);
    }

    public long getAccount() {
        return account;
    }

    public @NotNull URL getReference() {
        return reference;
    }
}
