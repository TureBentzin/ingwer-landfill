package de.bentzin.ingwer.landfill.client;

import de.bentzin.ingwer.landfill.netty.NettyTransport;
import de.bentzin.ingwer.landfill.netty.NettyUtils;
import de.bentzin.ingwer.landfill.netty.PacketRegistry;
import de.bentzin.ingwer.landfill.netty.packet.StringPacket;
import io.netty5.bootstrap.Bootstrap;
import io.netty5.channel.Channel;
import io.netty5.channel.ChannelInitializer;
import io.netty5.channel.ChannelOption;
import io.netty5.channel.MultithreadEventLoopGroup;
import io.netty5.channel.socket.SocketChannel;
import io.netty5.handler.ssl.SslContext;
import io.netty5.handler.ssl.SslContextBuilder;
import io.netty5.handler.ssl.util.InsecureTrustManagerFactory;
import io.netty5.handler.ssl.util.SelfSignedCertificate;
import io.netty5.util.concurrent.Future;
import io.netty5.util.concurrent.FutureCompletionStage;
import org.jetbrains.annotations.NotNull;

import javax.net.ssl.SSLException;
import java.net.InetSocketAddress;
import java.security.cert.CertificateException;
import java.util.Arrays;

/**
 * @author Ture Bentzin
 * 01.08.2023
 */
public class Client {

    public static final @NotNull PacketRegistry p = NettyUtils.newPacketRegistry();

    public static void main(String @NotNull [] args) throws InterruptedException, SSLException {
        System.out.println("Hello world! " + (args.length == 0 ? "" : Arrays.toString(args)));
        Thread.sleep(60);

        SelfSignedCertificate ssc = null;
        try {
            ssc = new SelfSignedCertificate();
        } catch (CertificateException e) {
            throw new RuntimeException(e);
        }

        final SslContext sslCtx = SslContextBuilder.forClient()
                .keyManager(ssc.certificate(), ssc.privateKey())
                .trustManager(InsecureTrustManagerFactory.INSTANCE).build();

        MultithreadEventLoopGroup
                workerLoop = NettyTransport.BEST.newMultithreadEventLoopGroup();

        Future<Channel> connect = new Bootstrap()
                .group(workerLoop)
                .remoteAddress(new InetSocketAddress("127.0.0.1", 2222))
                .channel(NettyTransport.BEST.channelClazz)
                .option(ChannelOption.TCP_NODELAY, true)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(@NotNull SocketChannel socketChannel) throws Exception {
                        socketChannel.pipeline().addLast(sslCtx.newHandler(socketChannel.bufferAllocator(), "127.0.0.1", 2222));
                        NettyUtils.initializePipeline(socketChannel.pipeline(), p);
                    }
                }).connect();
        FutureCompletionStage<Channel> await = connect.asStage().await();
        Channel channel = await.getNow();
        if(channel == null) {
            System.out.println("cant connect!");
            return;
        }
        System.out.println("connected to: " + channel.localAddress());

        channel.write(new StringPacket("Hello, World!"));
        channel.flush();


    }

}
