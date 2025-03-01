package com.chunkslab.gestures.playeranimator.nms.v1_20_R3.network;

import com.chunkslab.gestures.playeranimator.api.PlayerAnimator;
import com.chunkslab.gestures.playeranimator.api.model.player.PlayerModel;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import lombok.Getter;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.protocol.game.ClientboundRemoveEntitiesPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.entity.LevelEntityGetter;
import org.bukkit.entity.Entity;

import java.lang.reflect.Method;

public class PAChannelHandler extends ChannelDuplexHandler {

    private static Method entityGetter;

    static {
        for(var method : ServerLevel.class.getMethods()) {
            if(LevelEntityGetter.class.isAssignableFrom(method.getReturnType()) && method.getReturnType() != LevelEntityGetter.class) {
                entityGetter = method;
                break;
            }
        }
    }

    @Getter private final ServerPlayer player;

    public PAChannelHandler(ServerPlayer player) {
        this.player = player;
    }

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {

        if(msg instanceof ClientboundAddEntityPacket packet) {
            handleEntitySpawn(packet.getId());
        }else if(msg instanceof ClientboundRemoveEntitiesPacket packet) {
            for(int id : packet.getEntityIds()) {
                handleEntityDespawn(id);
            }
        }

        super.write(ctx, msg, promise);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        super.channelRead(ctx, msg);
    }

    private void handleEntitySpawn(int id) {
        final var entity = getEntityAsync(player.serverLevel(), id);
        PlayerModel model = PlayerAnimator.api.getModelManager().getPlayerModel(entity);
        if(model == null)
            return;
        model.spawn(player.getBukkitEntity());
    }

    private void handleEntityDespawn(int id) {
        final var entity = getEntityAsync(player.serverLevel(), id);
        PlayerModel model = PlayerAnimator.api.getModelManager().getPlayerModel(entity);
        if(model == null)
            return;
        model.despawn(player.getBukkitEntity());
    }

    private Entity getEntityAsync(ServerLevel world, int id) {
        final var entity = getEntityGetter(world).get(id);
        return entity == null ? null : entity.getBukkitEntity();
    }

    public static LevelEntityGetter<net.minecraft.world.entity.Entity> getEntityGetter(ServerLevel level) {
        if(entityGetter == null)
            return level.getEntities();
        try {
            return (LevelEntityGetter<net.minecraft.world.entity.Entity>) entityGetter.invoke(level);
        }catch (Throwable ignored) {
            return null;
        }
    }

}