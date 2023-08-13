package de.bentzin.ingwer.landfill.service;

import de.bentzin.ingwer.landfill.netty.NettyUtils;
import de.bentzin.ingwer.landfill.netty.PacketRegistry;
import io.netty5.channel.ChannelInitializer;
import io.netty5.channel.socket.SocketChannel;
import io.netty5.handler.ssl.SslContext;
import org.jetbrains.annotations.NotNull;


/**
 * @author Ture Bentzin
 * @since 2023-08-13
 */
public class ServerInit extends ChannelInitializer<SocketChannel> {

    private final @NotNull SslContext sslCtx;
    private final @NotNull PacketRegistry registry;


    public ServerInit(@NotNull SslContext sslCtx, @NotNull PacketRegistry registry) {
        this.sslCtx = sslCtx;
        this.registry = registry;
    }

    @Override
    protected void initChannel(@NotNull SocketChannel ch) throws Exception {
        System.out.println("New Channel: " + ch.remoteAddress());
        ch.pipeline().addLast(sslCtx.newHandler(ch.bufferAllocator()));
        NettyUtils.initializePipeline(ch.pipeline(), registry);
    }
}
