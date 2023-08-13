package de.bentzin.ingwer.landfill.handler.put;

import de.bentzin.ingwer.landfill.db.user.Account;
import de.bentzin.ingwer.landfill.netty.Packet;
import de.bentzin.ingwer.landfill.netty.PacketRegistry;
import de.bentzin.ingwer.landfill.netty.packet.put.PutAccountPacket;
import io.netty5.channel.ChannelHandlerContext;
import io.netty5.channel.SimpleChannelInboundHandler;
import org.jetbrains.annotations.NotNull;

/**
 * @author Ture Bentzin
 * @since 2023-08-13
 */
public class AccountHandler extends SimpleChannelInboundHandler<PutAccountPacket> {

    @Override
    protected void messageReceived(@NotNull ChannelHandlerContext ctx, @NotNull PutAccountPacket msg) throws Exception {
        Account account = new Account()
    }
}
