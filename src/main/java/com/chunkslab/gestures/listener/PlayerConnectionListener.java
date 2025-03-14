package com.chunkslab.gestures.listener;

import com.chunkslab.gestures.GesturesPlugin;
import com.chunkslab.gestures.api.player.GesturePlayer;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

@RequiredArgsConstructor
public class PlayerConnectionListener implements Listener {

    private final GesturesPlugin plugin;

    @EventHandler(priority = EventPriority.LOWEST)
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        plugin.getPlayerAnimator().injectPlayer(player);
        plugin.getScheduler().runTaskAsync(() -> {
            GesturePlayer gesturePlayer = plugin.getDatabase().loadPlayer(player.getUniqueId());
            gesturePlayer.setName(player.getName());
            plugin.getSkinTask().getSkinQueue().offer(gesturePlayer);
        });
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        GesturePlayer gesturePlayer = plugin.getPlayerManager().getPlayer(player.getUniqueId());
        plugin.getScheduler().runTaskAsync(() -> {
            if (gesturePlayer != null) {
                plugin.getDatabase().savePlayer(gesturePlayer);
                plugin.getPlayerManager().removePlayer(player.getUniqueId());
            }
        });
        plugin.getPlayerAnimator().removePlayer(player);
    }
}