package de.bentzin.ingwer.landfill.netty.packet.response;

import de.bentzin.ingwer.landfill.netty.packet.put.PutPacket;
import io.netty5.buffer.Buffer;
import org.jetbrains.annotations.NotNull;

/**
 * @author Ture Bentzin
 * @since 2023-08-14
 */
public final class PutConfirmResponsePacket extends ResponsePacket{

    private final PutPacket.@NotNull Datatype datatype;
    private final long packetChecksum;

    /**
     * The JobID the put belonged to (or -1)
     */
    private final long jobID;

    /**
     * only if the JobID was not -1 or lower
     * true if the job is confirmed completed now
     * false if the job is not completed
     */
    private final boolean closedNow;


    public PutConfirmResponsePacket(PutPacket.@NotNull Datatype datatype, long packetChecksum, long jobID, boolean closedNow) {
        super(ResponseType.PUT_CONFIRM);
        this.datatype = datatype;
        this.packetChecksum = packetChecksum;
        this.jobID = jobID;
        this.closedNow = closedNow;
    }

    public PutConfirmResponsePacket(@NotNull Buffer buffer) {
        super(buffer);
        datatype = PutPacket.Datatype.values()[buffer.readInt()];
        packetChecksum = buffer.readLong();
        jobID = buffer.readLong();
        closedNow = buffer.readBoolean();
        validate();
    }

    private void validate() throws IllegalStateException {
        if(jobID < 0 && closedNow) throw new IllegalStateException("cant close a non job!");
    }

    @Override
    public void encode(@NotNull Buffer buffer) {
        superEncode(buffer);
        buffer.writeInt(datatype.ordinal());
        buffer.writeLong(packetChecksum);
        buffer.writeLong(jobID);
        buffer.writeBoolean(closedNow);
        validate();
    }

    public long getPacketChecksum() {
        return packetChecksum;
    }

    public PutPacket.@NotNull Datatype getDatatype() {
        return datatype;
    }

    public long getJobID() {
        return jobID;
    }

    public boolean isClosedNow() {
        return closedNow;
    }
}
