package de.bentzin.ingwer.landfill.service;

import de.bentzin.ingwer.landfill.handler.put.AccountHandler;
import de.bentzin.ingwer.landfill.handler.put.GuildHandler;
import de.bentzin.ingwer.landfill.netty.NettyUtils;
import de.bentzin.ingwer.landfill.netty.PacketRegistry;
import io.netty5.channel.ChannelInitializer;
import io.netty5.channel.ChannelPipeline;
import io.netty5.channel.group.ChannelGroup;
import io.netty5.channel.socket.SocketChannel;
import io.netty5.handler.ssl.SslContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;


/**
 * @author Ture Bentzin
 * @since 2023-08-13
 */
public class ServerInit extends ChannelInitializer<SocketChannel> {

    private static final @NotNull Logger logger = LogManager.getLogger();

    private final @NotNull SslContext sslCtx;
    private final @NotNull PacketRegistry registry;
    private final ChannelGroup channelGroup;


    public ServerInit(@NotNull SslContext sslCtx, @NotNull PacketRegistry registry, @NotNull ChannelGroup channelGroup) {
        this.sslCtx = sslCtx;
        this.registry = registry;
        this.channelGroup = channelGroup;
    }

    @Override
    protected void initChannel(@NotNull SocketChannel ch) throws Exception {
        logger.info("New Channel: " + ch.remoteAddress() + " " + ch.id().asLongText());
        ch.pipeline().addLast(sslCtx.newHandler(ch.bufferAllocator()));
        channelGroup.add(ch);
        NettyUtils.initializePipeline(ch.pipeline(), registry);
        addServerHandlers(ch.pipeline());
    }

    public void addServerHandlers(@NotNull ChannelPipeline pipeline) {
        pipeline.addLast("put_account_handler",  new AccountHandler());
        pipeline.addLast("put_avatar_handler", new GuildHandler());
    }
}
