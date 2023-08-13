package de.bentzin.ingwer.landfill.netty.packet.put;

import de.bentzin.ingwer.landfill.Displayable;
import de.bentzin.ingwer.landfill.netty.Packet;
import io.netty5.buffer.Buffer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

/**
 * int: jobID (or -1)
 * int: datatype (ordinal)
 * int: length of the following bytes
 * following specific bytes
 *
 * @author Ture Bentzin
 * @since 2023-08-13
 */
public abstract class PutPacket implements Packet {

    protected static final @NotNull Logger logger = LogManager.getLogger();

    protected final int jobID;
    protected final @NotNull Datatype datatype;
    protected final int data_length;

    public PutPacket(@NotNull Buffer buffer) {
        jobID = buffer.readInt(); //jobID
        datatype = Datatype.values()[buffer.readInt()]; //datatype
        data_length =  buffer.readInt();
        logger.info("decoded a put-packet: Job: " + jobID + " datatype: " + datatype);
    }


    public void superEncode(@NotNull Buffer buffer, @NotNull Supplier<Integer> dataLength) {
        buffer.writeInt(jobID);
        buffer.writeInt(datatype.ordinal());
        buffer.writeInt(dataLength.get());
    }


    public static enum Datatype {
        ACCOUNT,
        //...
        ;
    }

}
