package de.bentzin.ingwer.landfill.handler.put;

import de.bentzin.ingwer.landfill.netty.packet.put.PutPacket;
import de.bentzin.ingwer.landfill.netty.packet.response.PutConfirmResponsePacket;
import de.bentzin.ingwer.landfill.tasks.TaskExecutionException;
import io.netty5.channel.Channel;
import org.jetbrains.annotations.NotNull;


/**
 * @author Ture Bentzin
 * @since 2023-08-17
 */
final class PutTaskUtils {

    private PutTaskUtils() {

    }

    public static void putConfirm(@NotNull Channel channel, PutPacket.@NotNull Datatype datatype, @NotNull PutPacket packet, int jobID, boolean closed) {
        channel.write(new PutConfirmResponsePacket(datatype, packet.getChecksum(), jobID, closed)); // TODO: Jobs
    }

    public static void putConfirm(@NotNull Channel channel, PutPacket.@NotNull Datatype datatype, @NotNull PutPacket packet) {
        channel.write(new PutConfirmResponsePacket(datatype, packet.getChecksum(), -1, true)); // TODO: Jobs
    }

    public static void putConfirm(@NotNull Channel channel, @NotNull PutPacket packet, int jobID, boolean closed) throws TaskExecutionException {
        channel.write(new PutConfirmResponsePacket(PutPacket.Datatype.findDatatype(packet.getClass()).orElseThrow(() -> new TaskExecutionException("packet has no datatype?")), packet.getChecksum(), jobID, closed)); // TODO: Jobs
    }

    public static void putConfirm(@NotNull Channel channel, @NotNull PutPacket packet) throws TaskExecutionException {
        channel.write(new PutConfirmResponsePacket(PutPacket.Datatype.findDatatype(packet.getClass()).orElseThrow(() -> new TaskExecutionException("packet has no datatype?")), packet.getChecksum(), -1, true)); // TODO: Jobs
    }
}
