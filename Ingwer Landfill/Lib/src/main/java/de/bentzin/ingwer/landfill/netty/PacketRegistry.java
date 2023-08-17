package de.bentzin.ingwer.landfill.netty;

import de.bentzin.tools.Independent;
import io.netty5.buffer.Buffer;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.HashMap;
import java.util.function.Function;

/**
 * @author Ture Bentzin
 * @since 2023-08-13
 */
public class PacketRegistry {

    private @NotNull HashMap<Integer, Class<? extends Packet>> packets = new HashMap<>();
    private @NotNull HashMap<Class<? extends Packet>, Integer> reversedPackets = new HashMap<>();


    private @NotNull HashMap<Integer, Function<Buffer, Packet>> constructors = new HashMap<>();

    protected boolean registerToMap(@NotNull Integer id, @NotNull Class<? extends  Packet> packetClass) {
        return !(packets.put(id, packetClass) == null && reversedPackets.put(packetClass, id) == null);
    }

    public void registerPacket(int id, @NotNull Class<? extends Packet> packetClass) throws NoSuchMethodException, IllegalAccessException {
        MethodHandle constructor = MethodHandles.lookup().findConstructor(packetClass, MethodType.methodType(void.class, Buffer.class));
        if(registerToMap(id, packetClass)) throw new IllegalStateException("Elements are already present!");
        constructors.put(id, buffer -> {
            try {
                return (Packet) constructor.invokeWithArguments(buffer);
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
        });
    }

    @ApiStatus.Experimental
    public void registerPacketSmart(@NotNull Class<? extends Packet> packetClass) throws NoSuchMethodException, IllegalAccessException {
        registerPacket(getNextIDKey(), packetClass);
    }

    @ApiStatus.Internal
    protected int getNextIDKey() {
        int maxKey = Integer.MIN_VALUE;
        for (Integer key : constructors.keySet()) {
            if (key > maxKey) {
                maxKey = key;
            }
        }
        return maxKey + 1;
    }

    public @NotNull HashMap<Class<? extends Packet>, Integer> getReversedPackets() {
        return reversedPackets;
    }

    public @NotNull HashMap<Integer, Class<? extends Packet>> getPackets() {
        return packets;
    }

    public @NotNull HashMap<Integer, Function<Buffer, Packet>> getConstructors() {
        return constructors;
    }
}
