package de.bentzin.ingwer.landfill.service;

import de.bentzin.ingwer.landfill.OneWaySwitch;
import de.bentzin.ingwer.landfill.backend.AuthorizedDumptruckOperator;
import de.bentzin.ingwer.landfill.backend.BackendHelper;
import de.bentzin.ingwer.landfill.netty.NettyTransport;
import de.bentzin.ingwer.landfill.netty.NettyUtils;
import de.bentzin.ingwer.landfill.netty.Packet;
import de.bentzin.ingwer.landfill.netty.PacketRegistry;
import de.bentzin.ingwer.landfill.netty.util.DumpTruckOperatorTrustManagerFactory;
import io.netty5.bootstrap.ServerBootstrap;
import io.netty5.channel.Channel;
import io.netty5.channel.ChannelOption;
import io.netty5.channel.MultithreadEventLoopGroup;
import io.netty5.channel.group.ChannelGroup;
import io.netty5.channel.group.DefaultChannelGroup;
import io.netty5.handler.ssl.ClientAuth;
import io.netty5.handler.ssl.SslContext;
import io.netty5.handler.ssl.SslContextBuilder;
import io.netty5.handler.ssl.util.SelfSignedCertificate;
import io.netty5.util.concurrent.FutureCompletionStage;
import io.netty5.util.concurrent.GlobalEventExecutor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.net.ssl.SSLException;
import java.net.SocketAddress;
import java.security.cert.CertificateException;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * @author Ture Bentzin
 * @since 2023-08-13
 */
public class Server {

    public static final @NotNull PacketRegistry PACKET_REGISTRY = NettyUtils.newPacketRegistry();
    private static final @NotNull Logger logger = LogManager.getLogger();
    private final @NotNull ChannelGroup channelGroup;
    private final @NotNull ServerBootstrap serverBootstrap;
    private final @NotNull SocketAddress socketAddress;
    private final @NotNull MultithreadEventLoopGroup bossGroup;
    private final @NotNull MultithreadEventLoopGroup workerGroup;

    private boolean running = false;

    private @NotNull OneWaySwitch wasRun = new OneWaySwitch();
    private @NotNull Channel channel;


    private Server(@NotNull ServerBootstrap serverBootstrap, @NotNull SocketAddress socketAddress,
                   @NotNull MultithreadEventLoopGroup bossGroup, @NotNull MultithreadEventLoopGroup workerGroup,
                   @NotNull ChannelGroup channelGroup) {
        this.serverBootstrap = serverBootstrap;

        this.socketAddress = socketAddress;
        this.bossGroup = bossGroup;
        this.workerGroup = workerGroup;
        this.channelGroup = channelGroup;
    }

    public static @NotNull Server create(@NotNull SocketAddress address) {
        SelfSignedCertificate ssc = null;
        try {
            ssc = new SelfSignedCertificate();
        } catch (CertificateException e) {
            throw new RuntimeException(e);
        }
        SslContext sslCtx;


        final DumpTruckOperatorTrustManagerFactory<AuthorizedDumptruckOperator> trustManagerFactory
                = DumpTruckOperatorTrustManagerFactory
                .<AuthorizedDumptruckOperator>builder("SHA-256")
                .fingerprints(BackendHelper.allAuthorizedOperators()).build();

        try {
            sslCtx = SslContextBuilder.forServer(ssc.certificate(), ssc.privateKey())
                    .clientAuth(ClientAuth.REQUIRE)
                    .trustManager(trustManagerFactory)
                    .build();
        } catch (SSLException e) {
            throw new RuntimeException(e);
        }

        MultithreadEventLoopGroup bossGroup = NettyTransport.BEST.newMultithreadEventLoopGroup(),
                workerGroup = NettyTransport.BEST.newMultithreadEventLoopGroup();

        DefaultChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

        ServerBootstrap bootstrap = new ServerBootstrap()
                .group(bossGroup, workerGroup)
                .localAddress(2222)
                .channel(NettyTransport.BEST.serverSocketChannelClazz)
                .childOption(ChannelOption.TCP_NODELAY, true)
                .childHandler(new ServerInit(sslCtx, PACKET_REGISTRY, channels));

        //Packets are not available at this point
        try {
            logger.info("Access to logon from: " + trustManagerFactory.getContainer().get());
        } catch (InterruptedException | ExecutionException e) {
            logger.error("Logon extraction failed: " + trustManagerFactory.getContainer());
            throw new RuntimeException(e);
        }

        logger.info("protocol supports the following packets:");
        for (Map.Entry<Integer, Class<? extends Packet>> integerClassEntry : PACKET_REGISTRY.getPackets().entrySet()) {
            logger.info(integerClassEntry.getKey() + "  " + integerClassEntry.getValue().getSimpleName());
        }

        return new Server(bootstrap, address, bossGroup, workerGroup, channels);
    }

    public void start() {
        if (wasRun.isFlipped()) {
            throw new IllegalStateException("the server was already run!");
        }
        wasRun.flip();

        try {
            channel = serverBootstrap.bind().asStage().await().getNow();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println("listening at " + channel.localAddress());

        running = true;
    }

    public void stop() throws InterruptedException {
        if (!running) return;

        if (channel != null) {
            logger.info("closing channel: " + channel.localAddress());
            FutureCompletionStage<Void> closed = channel.close().asStage().await();
            //logging and stuff
            logger.info("shutting down workers");
            FutureCompletionStage<Void> workerClose = workerGroup.shutdownGracefully().asStage().await();
            logger.info("shutting down bosses");
            FutureCompletionStage<Void> bossClose = bossGroup.shutdownGracefully().asStage().await();
            logger.info("Netty is down!");
        }
    }

    public @Nullable Channel getChannel() {
        return channel;
    }

    public @NotNull ChannelGroup getChannelGroup() {
        return channelGroup;
    }

    public @NotNull MultithreadEventLoopGroup getBossGroup() {
        return bossGroup;
    }

    public @NotNull MultithreadEventLoopGroup getWorkerGroup() {
        return workerGroup;
    }
}
