package com.chunkslab.gestures.nms.v1_21_R2.network;

import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.network.protocol.game.ServerboundPlayerInputPacket;

public class CGChannelHandler extends ChannelDuplexHandler {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof ServerboundPlayerInputPacket) {
            this.logSteer((ServerboundPlayerInputPacket)msg);
        }
        super.channelRead(ctx, msg);
    }

    private void logSteer(ServerboundPlayerInputPacket serverboundPlayerInputPacket) {
        boolean jump = serverboundPlayerInputPacket.input().jump();
        boolean shift = serverboundPlayerInputPacket.input().shift();
        thirdPersonController(jump, shift);
    }

    private void thirdPersonController(boolean moving, boolean exit) {
        
    }
}