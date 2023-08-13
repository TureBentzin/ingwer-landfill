package de.bentzin.ingwer.landfill.service;

import de.bentzin.ingwer.landfill.netty.NettyTransport;
import de.bentzin.ingwer.landfill.netty.NettyUtils;
import de.bentzin.ingwer.landfill.netty.PacketRegistry;
import io.netty5.bootstrap.ServerBootstrap;
import io.netty5.channel.Channel;
import io.netty5.channel.ChannelOption;
import io.netty5.channel.MultithreadEventLoopGroup;
import io.netty5.channel.group.ChannelGroup;
import io.netty5.channel.group.DefaultChannelGroup;
import io.netty5.handler.ssl.SslContext;
import io.netty5.handler.ssl.SslContextBuilder;
import io.netty5.handler.ssl.util.SelfSignedCertificate;
import io.netty5.util.concurrent.Future;
import io.netty5.util.concurrent.FutureCompletionStage;
import io.netty5.util.concurrent.GlobalEventExecutor;
import org.jetbrains.annotations.NotNull;

import javax.net.ssl.SSLException;
import java.security.cert.CertificateException;
import java.util.Arrays;

/**
 * @author Ture Bentzin
 * @since 2023-08-13
 */
public class Server {

    public static final @NotNull PacketRegistry p = NettyUtils.newPacketRegistry();
    private static final @NotNull ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    public static void main(String @NotNull [] args) throws InterruptedException {
        System.out.println("Hello world! " + (args.length == 0? "" : Arrays.toString(args)));

        SelfSignedCertificate ssc = null;
        try {
            ssc = new SelfSignedCertificate();
        } catch (CertificateException e) {
            throw new RuntimeException(e);
        }
        SslContext sslCtx;
        try {

            sslCtx = SslContextBuilder.forServer(ssc.certificate(), ssc.privateKey())
                    .build();
        } catch (SSLException e) {
            throw new RuntimeException(e);
        }

        MultithreadEventLoopGroup bossGroup = NettyTransport.BEST.newMultithreadEventLoopGroup(),
                workerLoop = NettyTransport.BEST.newMultithreadEventLoopGroup();

        Future<Channel> bind = new ServerBootstrap()
                .group(bossGroup, workerLoop)
                .localAddress(2222)
                .channel(NettyTransport.BEST.serverSocketChannelClazz)
                .childOption(ChannelOption.TCP_NODELAY, true)
                .childHandler(new ServerInit(sslCtx, p)).bind();
        FutureCompletionStage<Channel> await = bind.asStage().await();
        System.out.println("listening at " + await.getNow().localAddress());


    }
}
