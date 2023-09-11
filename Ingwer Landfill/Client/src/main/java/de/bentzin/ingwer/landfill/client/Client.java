package de.bentzin.ingwer.landfill.client;

import de.bentzin.ingwer.landfill.netty.NettyTransport;
import de.bentzin.ingwer.landfill.netty.NettyUtils;
import de.bentzin.ingwer.landfill.netty.PacketRegistry;
import de.bentzin.ingwer.landfill.netty.packet.KnockKnockPacket;
import de.bentzin.ingwer.landfill.netty.packet.put.PutAccountPacket;
import io.netty5.bootstrap.Bootstrap;
import io.netty5.channel.*;
import io.netty5.channel.socket.SocketChannel;
import io.netty5.handler.ssl.SslContext;
import io.netty5.handler.ssl.SslContextBuilder;
import io.netty5.handler.ssl.util.InsecureTrustManagerFactory;
import io.netty5.util.concurrent.Future;
import io.netty5.util.concurrent.FutureCompletionStage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;

/**
 * @author Ture Bentzin
 * 01.08.2023
 */
public class Client {

    public static final @NotNull PacketRegistry p = NettyUtils.newPacketRegistry();
    private static final @NotNull Logger logger = LogManager.getLogger();

    public static void main(String @NotNull [] args) throws InterruptedException, IOException, UnrecoverableKeyException, CertificateException, KeyStoreException, NoSuchAlgorithmException {
        ClientConfigManager clientConfigManager = new ClientConfigManager();
        Thread.sleep(60); //security delay

        Certificate certificate = CertificateFactory
                .getInstance("X.509")
                .generateCertificate(new FileInputStream(clientConfigManager.getCert()));


        final SslContext sslCtx = SslContextBuilder.forClient()
                .keyManager(clientConfigManager.getCert(), clientConfigManager.getPrivateKey())
                .trustManager(InsecureTrustManagerFactory.INSTANCE).build();

        MultithreadEventLoopGroup
                workerLoop = NettyTransport.BEST.newMultithreadEventLoopGroup();

        Future<Channel> connect = new Bootstrap()
                .group(workerLoop)
                .remoteAddress(new InetSocketAddress(clientConfigManager.getHostname(), clientConfigManager.getPort()))
                .channel(NettyTransport.BEST.channelClazz)
                .option(ChannelOption.TCP_NODELAY, true)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(@NotNull SocketChannel socketChannel) throws Exception {
                        socketChannel.pipeline().addLast(sslCtx.newHandler(socketChannel.bufferAllocator(),
                                clientConfigManager.getHostname(), clientConfigManager.getPort()));
                        NettyUtils.initializePipeline(socketChannel.pipeline(), p);
                        socketChannel.pipeline().addLast(new SimpleChannelInboundHandler<>() {
                            @Override
                            protected void messageReceived(@NotNull ChannelHandlerContext ctx, @NotNull Object msg) throws Exception {
                                logger.info("swallowing message: " + msg);
                            }
                        });
                    }
                }).connect();
        FutureCompletionStage<Channel> await = connect.asStage().await();
        Channel channel = await.getNow();
        if (channel == null) {
            logger.fatal("Cant connect to landfill!");
            return;
        }

        logger.info("Connected: "+ channel.id() + "@" + channel.remoteAddress());

        channel.write(new KnockKnockPacket(clientConfigManager.getWorkerID())); //KnockKnock
        channel.flush();

        //test
       /*
               channel.write(new PutAccountPacket(2103812381242342L, "USERNAME", "DISPLAYNAME", 9812347802134L,"LEGACYNAME#0000", "IT / TI", "I am a Test!", true));
        channel.flush();

        */


    }

}
