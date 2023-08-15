package de.bentzin.ingwer.landfill.handler.put;

import de.bentzin.ingwer.landfill.netty.packet.put.PutAccountPacket;
import de.bentzin.ingwer.landfill.netty.packet.put.PutAvatarPacket;
import io.netty5.channel.ChannelHandlerContext;
import io.netty5.channel.SimpleChannelInboundHandler;

/**
 * @author Ture Bentzin
 * @since 2023-08-15
 */
public class AvatarHandler extends SimpleChannelInboundHandler<PutAvatarPacket> {
    @Override
    protected void messageReceived(ChannelHandlerContext ctx, PutAvatarPacket msg) throws Exception {

    }
}
