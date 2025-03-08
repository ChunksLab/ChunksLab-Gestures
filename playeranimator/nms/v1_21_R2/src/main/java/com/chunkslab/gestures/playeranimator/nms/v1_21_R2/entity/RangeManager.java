package com.chunkslab.gestures.playeranimator.nms.v1_21_R2.entity;

import com.chunkslab.gestures.playeranimator.api.nms.IRangeManager;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerPlayerConnection;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

public class RangeManager implements IRangeManager {

    private final Object tracked; // Use Object instead of ChunkMap.TrackedEntity for full reflection

    public RangeManager(Object tracked) {
        this.tracked = tracked;
    }

    @Override
    public void addPlayer(Player player) {
        try {
            // Get the 'seenBy' field from the TrackedEntity class
            Field seenByField = getField(tracked.getClass(), "seenBy");
            seenByField.setAccessible(true);

            // Get the 'seenBy' Set from the tracked entity
            Set<ServerPlayerConnection> seenBy = (Set<ServerPlayerConnection>) seenByField.get(tracked);

            // Get the NMS player connection from the CraftPlayer
            ServerPlayer nmsPlayer = ((CraftPlayer) player).getHandle();

            // Add the player connection to the 'seenBy' Set
            seenBy.add(nmsPlayer.connection);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void removePlayer(Player player) {
        try {
            // Get the 'seenBy' field from the TrackedEntity class
            Field seenByField = getField(tracked.getClass(), "seenBy");
            seenByField.setAccessible(true);

            // Get the 'seenBy' Set from the tracked entity
            Set<ServerPlayerConnection> seenBy = (Set<ServerPlayerConnection>) seenByField.get(tracked);

            // Get the NMS player connection from the CraftPlayer
            ServerPlayer nmsPlayer = ((CraftPlayer) player).getHandle();

            // Add the player connection to the 'seenBy' Set
            seenBy.remove(nmsPlayer.connection);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setRenderDistance(int radius) {
        try {
            // Get the 'd' field (render distance) from the TrackedEntity class
            Field renderDistanceField = getField(tracked.getClass(), "d");
            renderDistanceField.setAccessible(true);

            // Set the render distance value
            renderDistanceField.setInt(tracked, radius);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Set<Player> getPlayerInRange() {
        Set<Player> players = new HashSet<>();
        try {
            // Get the 'seenBy' field from the TrackedEntity class
            Field seenByField = getField(tracked.getClass(), "seenBy");
            seenByField.setAccessible(true);

            // Get the 'seenBy' Set from the tracked entity
            Set<?> seenBy = (Set<?>) seenByField.get(tracked);

            // Iterate over the 'seenBy' Set and convert NMS players to Bukkit players
            for (Object playerConnection : seenBy) {
                Method getPlayerMethod = playerConnection.getClass().getMethod("getPlayer");
                Object nmsPlayer = getPlayerMethod.invoke(playerConnection);

                Method getBukkitEntityMethod = nmsPlayer.getClass().getMethod("getBukkitEntity");
                Player bukkitPlayer = (Player) getBukkitEntityMethod.invoke(nmsPlayer);

                players.add(bukkitPlayer);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return players;
    }

    // Helper method to get a field from a class (including private fields)
    private Field getField(Class<?> clazz, String fieldName) throws NoSuchFieldException {
        Field field = clazz.getDeclaredField(fieldName);
        field.setAccessible(true);
        return field;
    }
}