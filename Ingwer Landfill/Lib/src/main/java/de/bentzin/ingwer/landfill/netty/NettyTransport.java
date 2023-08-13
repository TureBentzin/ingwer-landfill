package de.bentzin.ingwer.landfill.netty;

import io.netty5.channel.IoHandlerFactory;
import io.netty5.channel.MultithreadEventLoopGroup;
import io.netty5.channel.epoll.Epoll;
import io.netty5.channel.epoll.EpollHandler;
import io.netty5.channel.epoll.EpollServerSocketChannel;
import io.netty5.channel.epoll.EpollSocketChannel;
import io.netty5.channel.kqueue.KQueue;
import io.netty5.channel.kqueue.KQueueHandler;
import io.netty5.channel.kqueue.KQueueServerSocketChannel;
import io.netty5.channel.kqueue.KQueueSocketChannel;
import io.netty5.channel.nio.NioHandler;
import io.netty5.channel.socket.ServerSocketChannel;
import io.netty5.channel.socket.SocketChannel;
import io.netty5.channel.socket.nio.NioServerSocketChannel;
import io.netty5.channel.socket.nio.NioSocketChannel;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.function.Supplier;

/**
 * @author Ture Bentzin
 * @since 2023-08-13
 */
public enum NettyTransport {

    KQUEUE(KQueue.isAvailable(), KQueueSocketChannel.class, KQueueServerSocketChannel.class, KQueueHandler::newFactory),
    EPOLL(Epoll.isAvailable(), EpollSocketChannel.class, EpollServerSocketChannel.class, EpollHandler::newFactory),
    NIO(true, NioSocketChannel.class, NioServerSocketChannel.class, NioHandler::newFactory);


    public static final @NotNull NettyTransport BEST = Arrays.stream(values()).filter(nettyTransport -> nettyTransport.available).findFirst().orElseThrow();

    @Contract(" -> new")
    public @NotNull MultithreadEventLoopGroup newMultithreadEventLoopGroup() {
        return new MultithreadEventLoopGroup(ioHandlerFactorySupplier.get());
    }

    public final boolean available;
    public final @NotNull Class<? extends SocketChannel> channelClazz;
    public final @NotNull Class<? extends ServerSocketChannel> serverSocketChannelClazz;
    public final @NotNull Supplier<IoHandlerFactory> ioHandlerFactorySupplier;

    NettyTransport(boolean available,
                   @NotNull Class<? extends SocketChannel> channelClazz,
                   @NotNull Class<? extends ServerSocketChannel> serverSocketChannelClazz,
                   @NotNull Supplier<IoHandlerFactory> ioHandlerFactorySupplier){
        this.available = available;
        this.channelClazz = channelClazz;

        this.serverSocketChannelClazz = serverSocketChannelClazz;
        this.ioHandlerFactorySupplier = ioHandlerFactorySupplier;
    }

}