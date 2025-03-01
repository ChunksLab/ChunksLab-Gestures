package com.chunkslab.gestures.playeranimator.nms.v1_20_R1.entity;

import com.chunkslab.gestures.playeranimator.api.nms.IRangeManager;
import com.chunkslab.gestures.playeranimator.api.utils.FieldUtils;
import net.minecraft.server.level.ChunkMap;
import org.bukkit.craftbukkit.v1_20_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;

public class RangeManager implements IRangeManager {

    private final ChunkMap.TrackedEntity tracked;

    public RangeManager(ChunkMap.TrackedEntity tracked) {
        this.tracked = tracked;
    }

    @Override
    public void addPlayer(Player player) {
        tracked.seenBy.add(((CraftPlayer) player).getHandle().connection);
    }

    @Override
    public void removePlayer(Player player) {
        tracked.seenBy.remove(((CraftPlayer) player).getHandle().connection);
    }

    @Override
    public void setRenderDistance(int radius) {
        try {
            FieldUtils.getField(ChunkMap.TrackedEntity.class, "d").setInt(tracked, radius);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Set<Player> getPlayerInRange() {
        Set<Player> list = new HashSet<>();
        tracked.seenBy.forEach(serverPlayerConnection -> list.add(serverPlayerConnection.getPlayer().getBukkitEntity()));
        return list;
    }
}
