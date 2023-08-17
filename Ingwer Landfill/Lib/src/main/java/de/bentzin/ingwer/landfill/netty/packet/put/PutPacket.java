package de.bentzin.ingwer.landfill.netty.packet.put;

import de.bentzin.ingwer.landfill.netty.BufferUtils;
import de.bentzin.ingwer.landfill.netty.Packet;
import io.netty5.buffer.Buffer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.TestOnly;

import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Optional;

/**
 * int: jobID (or -1)
 * int: datatype (ordinal)
 * int: length of the following bytes
 * following specific bytes
 *
 * @author Ture Bentzin
 * @since 2023-08-13
 */
public sealed abstract class PutPacket implements Packet permits PutAccountPacket, PutAvatarPacket, PutChannelPacket, PutGuildPacket {

    protected static final @NotNull Logger logger = LogManager.getLogger();

    protected final int jobID;
    protected final @NotNull Datatype datatype;

    public @Nullable long checksum = -1;

    public PutPacket(@NotNull Buffer buffer) {
        checksum = BufferUtils.calculateChecksum(buffer);
        jobID = buffer.readInt(); //jobID
        datatype = Datatype.values()[buffer.readInt()]; //datatype
        logger.info("decoded a put-packet: Job: " + jobID + " datatype: " + datatype);
    }

    @TestOnly
    private PutPacket(int jobID, @NotNull Datatype datatype) {
        this.jobID = jobID; //jobID
        this.datatype = datatype; //datatype
    }

    @TestOnly
    protected PutPacket(int jobID) {
        this.jobID = jobID; //jobID
        this.datatype = findDatatype().orElseThrow(() -> new NoSuchElementException(getClass().getSimpleName() + " seems to have no associated Datatype!")); //datatype
    }


    public final void encode(@NotNull Buffer buffer) {
        buffer.writeInt(jobID);
        buffer.writeInt(datatype.ordinal());
        encodePut(buffer);
    }

    @ApiStatus.OverrideOnly
    protected abstract void encodePut(@NotNull Buffer buffer);

    public long getChecksum() {
        return checksum;
    }

    public <T extends PutPacket> boolean checksumEquals(@NotNull T t) {
        return t.checksum == checksum;
    }

    public boolean checksumEquals(@NotNull Buffer buffer) {
        return BufferUtils.calculateChecksum(buffer.copy()) == checksum;
    }

    public boolean checksumEquals(long checksum) {
        return this.checksum == checksum;
    }

    public @NotNull Datatype getDatatype() {
        return datatype;
    }

    public static enum Datatype {
        ACCOUNT(PutAccountPacket.class),
        GUILD(PutGuildPacket.class),
        AVATAR(PutAccountPacket.class)
        //...
        ;
        private @NotNull Class<? extends PutPacket> packetClass;

        Datatype(@NotNull Class<? extends PutPacket> packetClass) {
            this.packetClass = packetClass;
        }

        public @NotNull Class<? extends PutPacket> getPacketClass() {
            return packetClass;
        }

        public static @NotNull Optional<Datatype> findDatatype(@NotNull Class<? extends PutPacket> packetClass) {
            return Arrays.stream(values()).filter(datatype1 -> datatype1.getPacketClass() == packetClass).findFirst();
        }
    }

    protected @NotNull Optional<Datatype> findDatatype() {
        return Datatype.findDatatype(getClass());
    }

}
