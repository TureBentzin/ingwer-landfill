package de.bentzin.ingwer.landfill.netty.packet.put;

import io.netty5.buffer.Buffer;
import org.jetbrains.annotations.NotNull;

/**
 * @author Ture Bentzin
 * @since 2023-08-17
 */
public final class PutGuildMembershipPacket extends PutPacket{

    private final long accountID;
    private final long guildID;

    public PutGuildMembershipPacket(@NotNull Buffer buffer) {
        super(buffer);
        accountID = buffer.readLong();
        guildID = buffer.readLong();
    }

    public PutGuildMembershipPacket(int jobID, long accountID, long guildID) {
        super(jobID);
        this.accountID = accountID;
        this.guildID = guildID;
    }

    @Override
    protected void encodePut(@NotNull Buffer buffer) {
        buffer.writeLong(accountID);
        buffer.writeLong(guildID);
    }


    public long getAccountID() {
        return accountID;
    }

    public long getGuildID() {
        return guildID;
    }
}
